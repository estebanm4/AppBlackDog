package com.dadm.appblackdog.database.data

import android.util.Log
import com.dadm.appblackdog.models.Breed
import com.dadm.appblackdog.services.GENERIC_TAG
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

interface BreedRepository {
    fun getAllBreedStream(): Flow<List<Breed>>

    fun getBreedStream(id: Int): Flow<Breed?>

    suspend fun insertBreed(data: Breed)
    suspend fun insertMultipleBreed(data: List<Breed>, update: Boolean = true)

    suspend fun deleteBreed(data: Breed)

    suspend fun updateBreed(data: Breed)
}

class BreedOfflineRepository(private val dao: BreedDao): BreedRepository{
    override fun getAllBreedStream(): Flow<List<Breed>> = dao.getAllItems()

    override fun getBreedStream(id: Int): Flow<Breed?> = dao.getItem(id)

    override suspend fun insertBreed(data: Breed) = dao.insert(data)

    override suspend fun insertMultipleBreed(data: List<Breed>, update: Boolean) {
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
            Log.d(GENERIC_TAG, "newData $newData updateData $updateData")
        }
    }

    override suspend fun deleteBreed(data: Breed) = dao.delete(data)

    override suspend fun updateBreed(data: Breed) = dao.update(data)

}