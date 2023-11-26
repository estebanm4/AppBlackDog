package com.dadm.appblackdog.database.data

import com.dadm.appblackdog.models.Owner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class OwnerOfflineRepository(private val ownerDao: OwnerDao) : OwnerRepository {
    override suspend fun getOwnerStream(): Flow<List<Owner>> = ownerDao.getAllItems()

    override suspend fun insertOwner(data: Owner) = ownerDao.insert(data)

    override suspend fun deleteOwner(data: Owner) = ownerDao.delete(data)

    override suspend fun updateOwner(data: Owner) = ownerDao.update(data)
    override suspend fun cleanOwners() {
        val localOwners = ownerDao.getAllItems()
        if (localOwners.first().isNotEmpty())
            localOwners.first().map {
                deleteOwner(it)
            }
    }
}