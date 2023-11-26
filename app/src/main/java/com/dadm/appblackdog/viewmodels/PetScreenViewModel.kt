package com.dadm.appblackdog.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dadm.appblackdog.database.data.AgeRangeRepository
import com.dadm.appblackdog.database.data.BreedRepository
import com.dadm.appblackdog.database.data.MeasureUnitRepository
import com.dadm.appblackdog.models.AgeRange
import com.dadm.appblackdog.models.Breed
import com.dadm.appblackdog.models.MeasureUnit
import com.dadm.appblackdog.models.UiPetForm
import com.dadm.appblackdog.services.FirebaseService
import com.dadm.appblackdog.services.GENERIC_TAG
import com.dadm.appblackdog.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date

class PetScreenViewModel(
    private val ageRangeRepository: AgeRangeRepository,
    private val breedRepository: BreedRepository,
    private val measureUnitRepository: MeasureUnitRepository,
    private val firebaseService: FirebaseService,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiPetForm())
    val uiState: StateFlow<UiPetForm> = _uiState.asStateFlow()

    // db data
    private var breedList: List<Breed> = mutableListOf()
    private var ageRangeList: List<AgeRange> = mutableListOf()
    private var measureUnitList: List<MeasureUnit> = mutableListOf()


    fun init() {
        Log.d(GENERIC_TAG, "inicia carga de datos **************")
        _uiState.value = UiPetForm()
        _uiState.update { ui -> ui.copy(isLoader = true) }
        viewModelScope.launch { loadData() }
    }

    private suspend fun loadData() = coroutineScope {
        val task = async {
            ageRangeList = ageRangeRepository.getAllAgeRangeStream().first()
            breedList = breedRepository.getAllBreedStream().first()
            measureUnitList =
                measureUnitRepository.getMeasureUnitStream(Constants.TYPE_WEIGHT).first()
        }.await()
        Log.d(
            GENERIC_TAG,
            "datos cargados ageRangeList ${ageRangeList.size}  breedList ${breedList.size}  measureUnitList ${measureUnitList.size} "
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
        _uiState.update { state -> state.copy(weight = data) }
    }

    fun updateMeasureUnitId(data: String) {
        //todo agregar logica
        _uiState.update { state -> state.copy(measureUnitId = data, measureUnit = data) }
    }

    fun updateBreedId(data: String) {
        //todo agregar logica
        _uiState.update { state -> state.copy(breedId = data, breed = data) }
    }

    fun updateAgeRangeId(data: String) {
        //todo agregar logica
        _uiState.update { state -> state.copy(ageRangeId = data, ageRange = data) }
    }

    @SuppressLint("SimpleDateFormat")
    fun updateBirthday(data: String) {
        var value = ""
        if (data.isNotEmpty()) {
            val date = Date(data.toLong() + 46400000)
            value = SimpleDateFormat("dd/MM/yyyy").format(date)
        }
        _uiState.update { state -> state.copy(birthdate = value) }
    }

    fun validateForm(context: Context) {

    }
}