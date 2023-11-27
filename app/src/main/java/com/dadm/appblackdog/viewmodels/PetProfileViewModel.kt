package com.dadm.appblackdog.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dadm.appblackdog.database.data.PetRepository
import com.dadm.appblackdog.models.Pet
import com.dadm.appblackdog.models.UiPetProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class PetProfileViewModel(
    private val petRepository: PetRepository,

    ) : ViewModel() {
    private val _uiState = MutableStateFlow(UiPetProfile())
    val uiState: StateFlow<UiPetProfile> = _uiState.asStateFlow()

    var pets: List<Pet> = listOf()

    fun refresh() {
        pets = listOf(Pet())
        _uiState.update { it.copy(loader = true, loadInfo = false) }
        viewModelScope.launch {
            loadData()
        }
    }

    fun changePet(name: String) {
        val updatePet = pets.first { it.name == name }
        _uiState.update {
            it.copy(
                name = updatePet.name,
                description = updatePet.description,
                breed = updatePet.breed,
                ageRange = updatePet.ageRange,
                weight = updatePet.weight,
                measureUnit = updatePet.measureUnit,
                photoUrl = updatePet.photoUrl,
                birthday = Date(updatePet.birthdate).toString(),
                )
        }
    }

    fun showLoader(show: Boolean) {
        _uiState.update { ui -> ui.copy(loader = show) }
    }

    private suspend fun loadData() = coroutineScope {
        pets = async(Dispatchers.IO) { petRepository.getAllPetStream().first() }.await()
        if (pets.isNotEmpty()) {
            val petsNames: List<String> = pets.map { it.name }
            val visiblePet = pets.first()
            _uiState.update {
                it.copy(
                    name = visiblePet.name,
                    description = visiblePet.description,
                    breed = visiblePet.breed,
                    ageRange = visiblePet.ageRange,
                    weight = visiblePet.weight,
                    measureUnit = visiblePet.measureUnit,
                    photoUrl = visiblePet.photoUrl,
                    birthday = Date(visiblePet.birthdate).toString(),
                    loader = false,
                    namesList = petsNames
                )
            }
        }

    }

}