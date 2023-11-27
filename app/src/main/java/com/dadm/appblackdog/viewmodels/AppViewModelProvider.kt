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
                ownerRepository = inventoryApplication().container.ownerRepository,
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
            PetAddViewModel(
                ageRangeRepository = inventoryApplication().container.ageRangeRepository,
                breedRepository = inventoryApplication().container.breedRepository,
                measureUnitRepository = inventoryApplication().container.measureUnitRepository,
                ownerRepository = inventoryApplication().container.ownerRepository,
                firebaseService = inventoryApplication().container.firebaseService,
            )
        }

        initializer {
            RecipeViewModel(
                recipeRepository = inventoryApplication().container.recipeRepository,
            )
        }

        initializer {
            PetProfileViewModel(
                petRepository = inventoryApplication().container.petRepository,
            )
        }

        initializer {
            InfoViewModel(
//                petRepository = inventoryApplication().container.petRepository,
            )
        }

        initializer {
            MainScreenViewModel(
                ageRangeRepository = inventoryApplication().container.ageRangeRepository,
                breedRepository = inventoryApplication().container.breedRepository,
                measureUnitRepository = inventoryApplication().container.measureUnitRepository,
                recipeRepository = inventoryApplication().container.recipeRepository,
                petRepository = inventoryApplication().container.petRepository,
                ownerRepository = inventoryApplication().container.ownerRepository,
                infoPostRepository = inventoryApplication().container.infoPostRepository,
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