package com.dadm.appblackdog.database

import android.content.Context
import com.dadm.appblackdog.database.data.AgeRangeOfflineRepository
import com.dadm.appblackdog.database.data.AgeRangeRepository
import com.dadm.appblackdog.database.data.OwnerOfflineRepository
import com.dadm.appblackdog.database.data.OwnerRepository
import com.dadm.appblackdog.services.FirebaseService

interface AppContainer {
    val ownerRepository: OwnerRepository
    val ageRangeRepository: AgeRangeRepository
    val firebaseService: FirebaseService
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val ownerRepository: OwnerRepository by lazy {
        OwnerOfflineRepository(AppBlackDogDatabase.getDatabase(context).ownerDao())
    }
    override val ageRangeRepository: AgeRangeRepository by lazy {
        AgeRangeOfflineRepository(AppBlackDogDatabase.getDatabase(context).ageRangeDao())
    }
    override val firebaseService: FirebaseService by lazy {
        FirebaseService()
    }
}