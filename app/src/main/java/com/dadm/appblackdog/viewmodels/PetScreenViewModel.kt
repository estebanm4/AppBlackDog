package com.dadm.appblackdog.viewmodels

import androidx.lifecycle.ViewModel
import com.dadm.appblackdog.models.UiPetForm
import com.dadm.appblackdog.services.FirebaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PetScreenViewModel(
    private val firebaseService: FirebaseService,

    ) : ViewModel() {
    private val _uiState = MutableStateFlow(UiPetForm())
    val uiState: StateFlow<UiPetForm> = _uiState.asStateFlow()


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
}