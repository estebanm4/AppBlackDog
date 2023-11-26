package com.dadm.appblackdog.viewmodels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dadm.appblackdog.LoginActivity
import com.dadm.appblackdog.MainActivity
import com.dadm.appblackdog.database.data.AgeRangeRepository
import com.dadm.appblackdog.database.data.BreedRepository
import com.dadm.appblackdog.database.data.MeasureUnitRepository
import com.dadm.appblackdog.models.AgeRange
import com.dadm.appblackdog.models.Breed
import com.dadm.appblackdog.models.MeasureUnit
import com.dadm.appblackdog.models.UiSplash
import com.dadm.appblackdog.services.GENERIC_TAG
import com.dadm.appblackdog.services.FirebaseService
import com.dadm.appblackdog.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel(
    private val ageRangeRepository: AgeRangeRepository,
    private val breedRepository: BreedRepository,
    private val measureUnitRepository: MeasureUnitRepository,
    private val firebaseService: FirebaseService,
) : ViewModel() {

    private var loadIsRun = false
    private val _uiState = MutableStateFlow(UiSplash())
    val uiState: StateFlow<UiSplash> = _uiState.asStateFlow()

    fun init() {
        if (!loadIsRun) {
            Log.d(GENERIC_TAG, "init *************")
            loadIsRun = true
            _uiState.update { ui -> ui.copy(navigate = false) }
            viewModelScope.launch { loadDataFromServer() }
        }
    }

    /** load common data from server*/
    private suspend fun loadDataFromServer() = coroutineScope {
        Log.d(GENERIC_TAG, "load data *************")
        launch(Dispatchers.IO) { getAndSaveAgeRanges() }
        launch(Dispatchers.IO) { getAndSaveBreeds() }
        launch(Dispatchers.IO) { getAndSaveMeasureUnits() }
        launch(Dispatchers.IO) { firebaseService.init() }
        withContext(Dispatchers.Main) {
            _uiState.update { ui -> ui.copy(navigate = true) }
        }
    }

    /** age ranges server and db process */
    private suspend fun getAndSaveAgeRanges(): Boolean {
        //variables
        val ageRangeSaveList = mutableListOf<AgeRange>()
        var success = false
        // get age ranges
        val ageRanges = firebaseService.getData(Constants.AGE_RANGES_TABLE_NAME)
        Log.d(GENERIC_TAG, "rangos de edad desde el servidor: ${ageRanges.size}")
        // when server return data generate a valid list to send to db
        if (ageRanges.isNotEmpty())
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
        Log.d(GENERIC_TAG, "rangos de edad desde el servidor: ${breeds.size}")
        // when server return data generate a valid list to send to db
        if (breeds.isNotEmpty())
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
        Log.d(GENERIC_TAG, "unidades de medida desde el servidor: ${measureUnits.size}")
        // when server return data generate a valid list to send to db
        if (measureUnits.isNotEmpty())
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

    fun navigateToScreen(context: Context) {
        Log.d(GENERIC_TAG, "start navigation *********************")
        if (isUserLogin()) navigateToDashboard(context)
        else navigateToLogin(context)
        loadIsRun = false
    }

    private fun isUserLogin(): Boolean = true
    private fun navigateToLogin(context: Context) {
//        val context = LocalContext.current
        context.startActivity(Intent(context, LoginActivity::class.java))
        (context as Activity).finish()
    }

    private fun navigateToDashboard(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
        (context as Activity).finish()
    }
}