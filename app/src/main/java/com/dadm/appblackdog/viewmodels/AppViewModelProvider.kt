package com.dadm.appblackdog.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dadm.appblackdog.AppBlackDog

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Initializer for LoginViewModel
        initializer {
            LoginViewModel(
                ownerRepository = inventoryApplication().container.ownerRepository,
                firebaseService = inventoryApplication().container.firebaseService,
            )
        }


        // Initializer for HomeViewModel
        initializer {
            SplashViewModel(
                ageRangeRepository = inventoryApplication().container.ageRangeRepository,
                breedRepository = inventoryApplication().container.breedRepository,
                measureUnitRepository = inventoryApplication().container.measureUnitRepository,
                firebaseService = inventoryApplication().container.firebaseService,
            )
        }

        initializer {
            RegisterViewModel(
                ownerRepository = inventoryApplication().container.ownerRepository,
                firebaseService = inventoryApplication().container.firebaseService,
            )
        }
        initializer {
            PetScreenViewModel(
                ageRangeRepository = inventoryApplication().container.ageRangeRepository,
                breedRepository = inventoryApplication().container.breedRepository,
                measureUnitRepository = inventoryApplication().container.measureUnitRepository,
                firebaseService = inventoryApplication().container.firebaseService,
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [AppBlackDog].
 */
fun CreationExtras.inventoryApplication(): AppBlackDog =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as AppBlackDog)