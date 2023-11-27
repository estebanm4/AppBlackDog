package com.dadm.appblackdog

import android.content.Context
import com.dadm.appblackdog.database.AppBlackDogDatabase
import com.dadm.appblackdog.database.data.AgeRangeOfflineRepository
import com.dadm.appblackdog.database.data.AgeRangeRepository
import com.dadm.appblackdog.database.data.BreedOfflineRepository
import com.dadm.appblackdog.database.data.BreedRepository
import com.dadm.appblackdog.database.data.InfoPostOfflineRepository
import com.dadm.appblackdog.database.data.InfoPostRepository
import com.dadm.appblackdog.database.data.MeasureUnitOfflineRepository
import com.dadm.appblackdog.database.data.MeasureUnitRepository
import com.dadm.appblackdog.database.data.OwnerOfflineRepository
import com.dadm.appblackdog.database.data.OwnerRepository
import com.dadm.appblackdog.database.data.PetOfflineRepository
import com.dadm.appblackdog.database.data.PetRepository
import com.dadm.appblackdog.database.data.RecipeOfflineRepository
import com.dadm.appblackdog.database.data.RecipeRepository
import com.dadm.appblackdog.services.FirebaseService

interface AppContainer {
    val ownerRepository: OwnerRepository
    val ageRangeRepository: AgeRangeRepository
    val breedRepository: BreedRepository
    val measureUnitRepository: MeasureUnitRepository
    val petRepository: PetRepository
    val recipeRepository: RecipeRepository
    val infoPostRepository: InfoPostRepository
    val firebaseService: FirebaseService
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val ownerRepository: OwnerRepository by lazy {
        OwnerOfflineRepository(AppBlackDogDatabase.getDatabase(context).ownerDao())
    }

    override val ageRangeRepository: AgeRangeRepository by lazy {
        AgeRangeOfflineRepository(AppBlackDogDatabase.getDatabase(context).ageRangeDao())
    }

    override val breedRepository: BreedRepository by lazy {
        BreedOfflineRepository(AppBlackDogDatabase.getDatabase(context).breedDao())
    }

    override val measureUnitRepository: MeasureUnitRepository by lazy {
        MeasureUnitOfflineRepository(AppBlackDogDatabase.getDatabase(context).measureUnitDao())
    }

    override val petRepository: PetRepository by lazy {
        PetOfflineRepository(AppBlackDogDatabase.getDatabase(context).petDao())
    }

    override val recipeRepository: RecipeRepository by lazy {
        RecipeOfflineRepository(AppBlackDogDatabase.getDatabase(context).recipeDao())
    }

    override val infoPostRepository: InfoPostRepository by lazy {
        InfoPostOfflineRepository(AppBlackDogDatabase.getDatabase(context).infoPostDao())
    }

    override val firebaseService: FirebaseService by lazy {
        FirebaseService()
    }
}