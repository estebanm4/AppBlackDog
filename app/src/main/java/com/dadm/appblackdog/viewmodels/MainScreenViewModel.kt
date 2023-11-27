package com.dadm.appblackdog.viewmodels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dadm.appblackdog.LoginActivity
import com.dadm.appblackdog.database.data.AgeRangeRepository
import com.dadm.appblackdog.database.data.BreedRepository
import com.dadm.appblackdog.database.data.InfoPostRepository
import com.dadm.appblackdog.database.data.MeasureUnitRepository
import com.dadm.appblackdog.database.data.OwnerRepository
import com.dadm.appblackdog.database.data.PetRepository
import com.dadm.appblackdog.database.data.RecipeRepository
import com.dadm.appblackdog.models.AgeRange
import com.dadm.appblackdog.models.Breed
import com.dadm.appblackdog.models.InfoPost
import com.dadm.appblackdog.models.MeasureUnit
import com.dadm.appblackdog.models.Pet
import com.dadm.appblackdog.models.Recipe
import com.dadm.appblackdog.services.FirebaseService
import com.dadm.appblackdog.services.GENERIC_TAG
import com.dadm.appblackdog.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val ageRangeRepository: AgeRangeRepository,
    private val breedRepository: BreedRepository,
    private val measureUnitRepository: MeasureUnitRepository,
    private val recipeRepository: RecipeRepository,
    private val petRepository: PetRepository,
    private val infoPostRepository: InfoPostRepository,
    private val firebaseService: FirebaseService,
    private val ownerRepository: OwnerRepository,
) : ViewModel() {

    var loadData = true

    fun init() {
        if (loadData) {
            loadData = false
            viewModelScope.launch {
                val users =
                    async(Dispatchers.IO) { ownerRepository.getOwnerStream().first() }.await()
                if (users.isNotEmpty()) loadServerData(users.first().serverId)
            }
        }
    }

    /**LogOut*/

    fun logOut(context: Context) {
        // clean session data
        viewModelScope.launch {
            // clean user info in db
            ownerRepository.cleanOwners()
            // clean pets
            petRepository.cleanPets()
            // end session in firebase auth
            firebaseService.userLogOut()
            // navigate to register screen
            navigateToLogin(context)
        }
    }

    private fun navigateToLogin(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java))
        (context as Activity).finish()
    }

    /** server methods */
    private suspend fun loadServerData(ownerId: String) = coroutineScope {
        launch(Dispatchers.IO) { getAndSaveAgeRanges() }
        launch(Dispatchers.IO) { getAndSaveBreeds() }
        launch(Dispatchers.IO) { getAndSaveMeasureUnits() }
        launch(Dispatchers.IO) { getAndSaveRecipes() }
        launch(Dispatchers.IO) { getAndSaveInfoPost() }
        launch(Dispatchers.IO) { getAndSavePets(ownerId) }
    }

    /** age ranges server and db process */
    private suspend fun getAndSaveAgeRanges(): Boolean {
        //variables
        val ageRangeSaveList = mutableListOf<AgeRange>()
        var success = false
        // get age ranges
        val ageRanges = firebaseService.getData(Constants.AGE_RANGES_TABLE_NAME)
        Log.d(GENERIC_TAG, "rangos de edad desde el servidor: ${ageRanges?.size}")
        // when server return data generate a valid list to send to db
        if (!ageRanges.isNullOrEmpty())
            ageRanges.map {
                ageRangeSaveList.add(
                    AgeRange(
                        serverId = it.id,
                        max = (it.data["max"] as Long? ?: 0).toInt(),
                        min = (it.data["min"] as Long? ?: 0).toInt(),
                        description = it.data["description"] as String? ?: "",
                    )
                )
            }
        // save agesRanges in db
        if (ageRangeSaveList.isNotEmpty()) {
            ageRangeRepository.insertMultipleAgeRange(data = ageRangeSaveList)
            success = true
        }
        return success
    }

    /** breeds server and db process */
    private suspend fun getAndSaveBreeds(): Boolean {
        //variables
        val breedsSaveList = mutableListOf<Breed>()
        var success = false
        // get breeds from server
        val breeds = firebaseService.getData(Constants.BREEDS_TABLE_NAME)
        Log.d(GENERIC_TAG, "razas desde el servidor: ${breeds?.size}")
        // when server return data generate a valid list to send to db
        if (!breeds.isNullOrEmpty())
            breeds.map {
                breedsSaveList.add(
                    Breed(
                        serverId = it.id,
                        name = it.data["name"] as String? ?: "",
                        small = it.data["small"] as Boolean? ?: false,
                    )
                )
            }
        // save breeds in db
        if (breedsSaveList.isNotEmpty()) {
            breedRepository.insertMultipleBreed(data = breedsSaveList)
            success = true
        }
        return success
    }

    /** measure units server and db process */
    private suspend fun getAndSaveMeasureUnits(): Boolean {
        //variables
        val measureUnitSaveList = mutableListOf<MeasureUnit>()
        var success = false
        // get measure units from server
        val measureUnits = firebaseService.getData(Constants.MEASURE_UNIT_TABLE_NAME)
        Log.d(GENERIC_TAG, "unidades de medida desde el servidor: ${measureUnits?.size}")
        // when server return data generate a valid list to send to db
        if (!measureUnits.isNullOrEmpty())
            measureUnits.map {
                measureUnitSaveList.add(
                    MeasureUnit(
                        serverId = it.id,
                        name = it.data["name"] as String? ?: "",
                        abr = it.data["abr"] as String? ?: "",
                        type = it.data["type"] as String? ?: "",
                        conversion = (it.data["conversion"] as String? ?: "0").toDouble(),
                        parent = it.data["parent"] as Boolean? ?: false,
                    )
                )
            }
        // save measure units in db
        if (measureUnitSaveList.isNotEmpty()) {
            measureUnitRepository.insertMultipleMeasureUnit(data = measureUnitSaveList)
            success = true
        }
        return success
    }

    /** recipes server and db process */
    private suspend fun getAndSaveRecipes(): Boolean {
        //variables
        val recipeSaveList = mutableListOf<Recipe>()
        var success = false
        // get recipes from server
        val recipes = firebaseService.getData(Constants.RECIPE_TABLE_NAME)
        Log.d(GENERIC_TAG, "recetas el servidor: ${recipes?.size}")
        // when server return data generate a valid list to send to db
        if (!recipes.isNullOrEmpty())
            recipes.map {
                recipeSaveList.add(
                    Recipe(
                        serverId = it.id,
                        name = it.data["name"] as String? ?: "",
                        description = it.data["description"] as String? ?: "",
                        items = it.data["items"] as String? ?: "",
                        imageUrl = it.data["imageUrl"] as String? ?: "",
                    )
                )
            }
        // save recipes in db
        if (recipeSaveList.isNotEmpty()) {
            recipeRepository.insertMultipleRecipe(data = recipeSaveList)
            success = true
        }
        return success
    }

    /** pets server and db process */
    private suspend fun getAndSavePets(ownerId: String): Boolean {
        //variables
        val petSaveList = mutableListOf<Pet>()
        var success = false
        // get pets from server
        val pets = firebaseService.getDataByArgument(
            reference = Constants.PET_TABLE_NAME,
            argument = "ownerId",
            value = ownerId
        )
        Log.d(GENERIC_TAG, "Mascotas desde el servidor: ${pets?.size}")
        // when server return data generate a valid list to send to db
        if (!pets.isNullOrEmpty())
            pets.map {
                petSaveList.add(
                    Pet(
                        serverId = it.id,
                        name = it.data["name"] as String? ?: "",
                        description = it.data["description"] as String? ?: "",
                        ownerId = it.data["ownerId"] as String? ?: "",
                        photoUrl = it.data["photoUrl"] as String? ?: "",
                        ageRangeId = it.data["ageRangeId"] as String? ?: "",
                        ageRange = it.data["ageRange"] as String? ?: "",
                        breedId = it.data["breedId"] as String? ?: "",
                        breed = it.data["breed"] as String? ?: "",
                        weight = it.data["weight"] as String? ?: "",
                        measureUnitId = it.data["measureUnitId"] as String? ?: "",
                        measureUnit = it.data["measureUnit"] as String? ?: "",
                        birthdate = it.data["birthdate"] as Long? ?: 0L,
                    )
                )
            }
        // save pets in db
        if (petSaveList.isNotEmpty()) {
            petRepository.insertMultiplePet(data = petSaveList)
            success = true
        }
        return success
    }


    /** recipes server and db process */
    private suspend fun getAndSaveInfoPost(): Boolean {
        //variables
        val postSaveList = mutableListOf<InfoPost>()
        var success = false
        // get recipes from server
        val posts = firebaseService.getData(Constants.INFO_POST_TABLE_NAME)
        Log.d(GENERIC_TAG, "post desde el servidor: ${posts?.size}")
        // when server return data generate a valid list to send to db
        if (!posts.isNullOrEmpty())
            posts.map {
                postSaveList.add(
                    InfoPost(
                        serverId = it.id,
                        name = it.data["name"] as String? ?: "",
                        description = it.data["description"] as String? ?: "",
                    )
                )
            }
        // save recipes in db
        if (postSaveList.isNotEmpty()) {
            infoPostRepository.insertMultipleInfoPost(data = postSaveList)
            success = true
        }
        return success
    }
}