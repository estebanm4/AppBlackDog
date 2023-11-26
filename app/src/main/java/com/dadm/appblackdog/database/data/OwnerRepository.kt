package com.dadm.appblackdog.database.data

import com.dadm.appblackdog.models.Owner
import kotlinx.coroutines.flow.Flow

interface OwnerRepository {
    suspend fun getOwnerStream(): Flow<List<Owner>>

    suspend fun insertOwner(data: Owner)

    suspend fun deleteOwner(data: Owner)

    suspend fun updateOwner(data: Owner)
    suspend fun cleanOwners()
}