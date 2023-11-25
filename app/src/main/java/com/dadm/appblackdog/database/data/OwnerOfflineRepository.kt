package com.dadm.appblackdog.database.data

import com.dadm.appblackdog.models.Owner
import kotlinx.coroutines.flow.Flow

class OwnerOfflineRepository(private val ownerDao: OwnerDao) : OwnerRepository {
    override fun getOwnerStream(serverId: String): Flow<Owner?> = ownerDao.getItem(serverId)

    override suspend fun insertOwner(data: Owner) = ownerDao.insert(data)

    override suspend fun deleteOwner(data: Owner) = ownerDao.delete(data)

    override suspend fun updateOwner(data: Owner) = ownerDao.update(data)
}