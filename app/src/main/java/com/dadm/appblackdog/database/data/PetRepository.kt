package com.dadm.appblackdog.database.data

import android.util.Log
import com.dadm.appblackdog.models.Pet
import com.dadm.appblackdog.services.GENERIC_TAG
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

interface PetRepository {
    fun getAllPetStream(): Flow<List<Pet>>

    fun getPetStream(serverId: String): Flow<Pet?>

    suspend fun insertPet(data: Pet)
    suspend fun insertMultiplePet(data: List<Pet>, update: Boolean = true)

    suspend fun deletePet(data: Pet)

    suspend fun updatePet(data: Pet)
}

class PetOfflineRepository(private val dao: PetDao): PetRepository{
    override fun getAllPetStream(): Flow<List<Pet>> = dao.getAllItems()

    override fun getPetStream(serverId: String): Flow<Pet?> = dao.getItem(serverId)

    override suspend fun insertPet(data: Pet) = dao.insert(data)

    override suspend fun insertMultiplePet(data: List<Pet>, update: Boolean) {
        // counts for actions in db
        var newData = 0
        var updateData = 0
        coroutineScope {
            // validate thet list is not empty
            if (data.isNotEmpty())
                data.forEach() {
                    // search if item exist in db
                    val count = async { dao.checkItemByServerId(it.serverId).first() }.await()
                    // when update is enabled reload data in db
                    if (count > 0 && update) {
                        val id = async { dao.getItemId(it.serverId).first() }.await()
                        val item = it.copy(id = id)
                        launch { dao.update(item) }
                        updateData++
                    }
                    // add new item to db
                    else if (count == 0) {
                        launch { dao.insert(it) }
                        newData++
                    }
                }
            Log.d(GENERIC_TAG, "pets newData $newData updateData $updateData")
        }
    }

    override suspend fun deletePet(data: Pet) = dao.delete(data)

    override suspend fun updatePet(data: Pet) = dao.update(data)


}