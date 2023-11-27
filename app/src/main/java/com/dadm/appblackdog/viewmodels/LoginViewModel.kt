package com.dadm.appblackdog.viewmodels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dadm.appblackdog.MainActivity
import com.dadm.appblackdog.R
import com.dadm.appblackdog.database.data.AgeRangeRepository
import com.dadm.appblackdog.database.data.BreedRepository
import com.dadm.appblackdog.database.data.MeasureUnitRepository
import com.dadm.appblackdog.database.data.OwnerRepository
import com.dadm.appblackdog.database.data.RecipeRepository
import com.dadm.appblackdog.models.AgeRange
import com.dadm.appblackdog.models.Breed
import com.dadm.appblackdog.models.MeasureUnit
import com.dadm.appblackdog.models.Owner
import com.dadm.appblackdog.models.Recipe
import com.dadm.appblackdog.models.UiLogin
import com.dadm.appblackdog.services.FirebaseService
import com.dadm.appblackdog.services.GENERIC_TAG
import com.dadm.appblackdog.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val ageRangeRepository: AgeRangeRepository,
    private val breedRepository: BreedRepository,
    private val measureUnitRepository: MeasureUnitRepository,
    private val recipeRepository: RecipeRepository,
    private val ownerRepository: OwnerRepository,
    private val firebaseService: FirebaseService,
) : ViewModel() {
    //variables
    private val _uiState = MutableStateFlow(UiLogin())
    val uiState: StateFlow<UiLogin> = _uiState.asStateFlow()


    fun startLogin(context: Context) {
        //if form is valid start login
        if (validateFields()) {
            // enabled loader
            showLoader(true)
            // start background process
            viewModelScope.launch {
                loginProcess(context)
                loadServerData()
            }

        }
    }

    /** start user login*/
    private suspend fun loginProcess(context: Context) = coroutineScope {
        // auth firebase
        val firebaseJob = async(Dispatchers.IO) {
            firebaseService.emailLogin(data = uiState.value)
        }.await()

        // update UI
        if (firebaseJob) {
            // load all data data from owner table in firestore (required for obtain serverId)
            val user = async(Dispatchers.IO) {
                firebaseService.getDataByArgument(
                    reference = Constants.OWNER_TABLE_NAME,
                    argument = "email",
                    value = _uiState.value.email
                )
            }.await()
            Log.d(GENERIC_TAG, "load user info ${user?.isNotEmpty()}")
            // finally save the data in local db and finish user register
            if (!user.isNullOrEmpty()) {
                val loginUser = user.first()
                val ownerDb = Owner(
                    serverId = loginUser.id,
                    name = loginUser.data["name"] as String,
                    lastname = loginUser.data["lastname"] as String,
                    email = loginUser.data["email"] as String,
                    hasPets = loginUser.data["hasPets"] as Boolean,
                    photoUrl = loginUser.data["photoUrl"] as String,
                )
                // clean owner from last session
                ownerRepository.cleanOwners()
                // insert new owner data
                ownerRepository.insertOwner(ownerDb)
                Log.d(GENERIC_TAG, "Login process end ************")
                // when all task is ok, start navigation to main screen
                navigateToMainScreen(context)
                // reset data in viewModel
                reset()
            }
        } else {
            // Notify the user the cause of login error
            showLoginAlert(context)
            // disabled loader
            showLoader(false)
        }
    }

    fun updateEmail(data: String) {
        _uiState.update { state -> state.copy(email = data) }
    }

    fun updatePassword(data: String) {
        _uiState.update { state -> state.copy(password = data) }
    }

    private fun navigateToMainScreen(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
        (context as Activity).finish()
    }

    private fun showLoginAlert(context: Context) {
        Toast.makeText(
            context,
            ContextCompat.getString(context, R.string.server_error),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showLoader(show: Boolean) {
        _uiState.update { state -> state.copy(isLoaderEnable = show) }
    }

    private fun validateFields(): Boolean {
        return uiState.value.email.isNotEmpty() && uiState.value.password.isNotEmpty()
    }

    private fun reset() {
        _uiState.update { currentState ->
            currentState.copy(
                email = "",
                password = "",
                isLoaderEnable = false
            )
        }
    }

///
    private suspend fun loadServerData() = coroutineScope {
    launch(Dispatchers.IO) { getAndSaveAgeRanges() }
    launch(Dispatchers.IO) { getAndSaveBreeds() }
    launch(Dispatchers.IO) { getAndSaveMeasureUnits() }
    launch(Dispatchers.IO) { getAndSaveRecipes() }
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
        // get age ranges
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
        // save agesRanges in db
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
        // get age ranges
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
        // save agesRanges in db
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
        // get age ranges
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
        // save agesRanges in db
        if (recipeSaveList.isNotEmpty()) {
            recipeRepository.insertMultipleRecipe(data = recipeSaveList)
            success = true
        }
        return success
    }

}