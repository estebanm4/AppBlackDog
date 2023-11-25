package com.dadm.appblackdog.database.data

import com.dadm.appblackdog.models.Owner
import kotlinx.coroutines.flow.Flow

interface OwnerRepository {
    /**
     * Retrieve an item from the given data source that matches with the [serverId].
     */
    fun getOwnerStream(serverId: String): Flow<Owner?>

    /**
     * Insert item in the data source
     */
    suspend fun insertOwner(data: Owner)

    /**
     * Delete item from the data source
     */
    suspend fun deleteOwner(data: Owner)

    /**
     * Update item in the data source
     */
    suspend fun updateOwner(data: Owner)
}