package com.dadm.appblackdog.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.dadm.appblackdog.R
import com.dadm.appblackdog.database.data.AgeRangeRepository
import com.dadm.appblackdog.database.data.BreedRepository
import com.dadm.appblackdog.database.data.MeasureUnitRepository
import com.dadm.appblackdog.database.data.OwnerRepository
import com.dadm.appblackdog.models.AgeRange
import com.dadm.appblackdog.models.BlackDogNavigationRoutes
import com.dadm.appblackdog.models.Breed
import com.dadm.appblackdog.models.MeasureUnit
import com.dadm.appblackdog.models.Owner
import com.dadm.appblackdog.models.UiPetForm
import com.dadm.appblackdog.services.FirebaseService
import com.dadm.appblackdog.services.GENERIC_TAG
import com.dadm.appblackdog.utils.Constants
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class PetAddViewModel(
    private val ageRangeRepository: AgeRangeRepository,
    private val breedRepository: BreedRepository,
    private val measureUnitRepository: MeasureUnitRepository,
    private val ownerRepository: OwnerRepository,
    private val firebaseService: FirebaseService,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiPetForm())
    val uiState: StateFlow<UiPetForm> = _uiState.asStateFlow()

    // db data
    private var owner: Owner = Owner()
    private var breedList: List<Breed> = mutableListOf()
    private var ageRangeList: List<AgeRange> = mutableListOf()
    private var measureUnitList: List<MeasureUnit> = mutableListOf()


    fun init() {
        Log.d(GENERIC_TAG, "inicia carga de datos **************")
        _uiState.value = UiPetForm()
        showLoader(true)
        viewModelScope.launch { loadData() }
    }

    private fun showLoader(show: Boolean) {
        _uiState.update { ui -> ui.copy(isLoader = show) }
    }

    private suspend fun loadData() = coroutineScope {
        var ownerList = listOf<Owner>()
        val task = async {
            ownerList = ownerRepository.getOwnerStream().first()
            ageRangeList = ageRangeRepository.getAllAgeRangeStream().first()
            ageRangeList = ageRangeRepository.getAllAgeRangeStream().first()
            breedList = breedRepository.getAllBreedStream().first()
            measureUnitList =
                measureUnitRepository.getMeasureUnitStream(Constants.TYPE_WEIGHT).first()
        }.await()
        if (ownerList.isNotEmpty()) owner = ownerList.first()
        Log.d(
            GENERIC_TAG,
            "datos cargados owner ${owner.serverId} ${owner.name} ageRangeList ${ageRangeList.size}  breedList ${breedList.size}  measureUnitList ${measureUnitList.size} "
        )

        val units = mutableListOf<String>()
        measureUnitList.map { units.add(it.abr) }
        val breeds = mutableListOf<String>()
        breedList.map { breeds.add(it.name) }
        val ages = mutableListOf<String>()
        ageRangeList.map { ages.add(it.description) }
        Log.d(
            GENERIC_TAG,
            "finaliza y prepara ui ${ages.size}  breeds ${breeds.size}  units ${units.size} "
        )
        _uiState.update { ui ->
            ui.copy(
                isLoader = false,
                measureUnitList = units,
                breedList = breeds,
                ageRangeList = ages
            )
        }
    }

    fun updateName(data: String) {
        _uiState.update { state -> state.copy(name = data, nameError = data.isEmpty()) }
    }

    fun updateDescription(data: String) {
        _uiState.update { state -> state.copy(description = data) }
    }

    fun updateWeight(data: String) {
        _uiState.update { state -> state.copy(weight = data, weightError = data.isEmpty()) }
    }

    fun updateMeasureUnitId(data: String) {
        val measureUnit = measureUnitList.first { it.abr == data }
        _uiState.update { state ->
            state.copy(
                measureUnitId = measureUnit.serverId,
                measureUnit = measureUnit.abr,
                measureUnitError = false
            )
        }
    }

    fun updateBreedId(data: String) {
        val breed = breedList.first { it.name == data }
        _uiState.update { state ->
            state.copy(
                breedId = breed.serverId,
                breed = breed.name,
                breedError = false
            )
        }
    }

    fun updateAgeRangeId(data: String) {
        val ageRange = ageRangeList.first { it.description == data }
        _uiState.update { state ->
            state.copy(
                ageRangeId = ageRange.serverId,
                ageRange = ageRange.description,
                ageRangeError = false
            )
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun updateBirthday(data: String) {
        var value = ""
        var time = 0L
        if (data.isNotEmpty()) {
            val date = Date(data.toLong() + 46400000)
            value = SimpleDateFormat("dd/MM/yyyy").format(date)
            time = data.toLong()
        }
        _uiState.update { state ->
            state.copy(
                birthdate = value,
                birthdateTimeStamp = time,
                birthdateError = time == 0L
            )
        }
    }

    fun validateForm(context: Context, navController: NavController) {
        if (_uiState.value.validateForm()) {
            showLoader(true)
            viewModelScope.launch { createPet(context, navController) }
        } else {
            _uiState.update { ui ->
                ui.copy(
                    nameError = ui.name.isEmpty(),
                    breedError = ui.breedId.isEmpty(),
                    ageRangeError = ui.ageRangeId.isEmpty(),
                    weightError = ui.weight.isEmpty(),
                    measureUnitError = ui.measureUnitId.isEmpty(),
                    birthdateError = ui.birthdateTimeStamp == 0L
                )
            }
        }
    }

    private suspend fun createPet(context: Context, navController: NavController) = coroutineScope {
        val newPet = _uiState.value.toPetMap(owner.serverId)
        val task =
            async {
                firebaseService.setData(
                    reference = Constants.PET_TABLE_NAME,
                    data = newPet
                )
            }.await()
        if (task) {
            Toast.makeText(
                context,
                ContextCompat.getString(context, R.string.create_pet),
                Toast.LENGTH_SHORT
            ).show()
            navController.popBackStack(
                BlackDogNavigationRoutes.UserData.name,
                inclusive = false
            )
        }
        showLoader(false)
    }
}