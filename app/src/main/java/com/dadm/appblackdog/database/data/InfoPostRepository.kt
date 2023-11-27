package com.dadm.appblackdog.database.data

import android.util.Log
import com.dadm.appblackdog.models.InfoPost
import com.dadm.appblackdog.services.GENERIC_TAG
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

interface InfoPostRepository {
    fun getAllInfoPostStream(): Flow<List<InfoPost>>

    fun getInfoPostStream(serverId: String): Flow<InfoPost?>

    suspend fun insertInfoPost(data: InfoPost)
    suspend fun insertMultipleInfoPost(data: List<InfoPost>, update: Boolean = true)

    suspend fun deleteInfoPost(data: InfoPost)

    suspend fun updateInfoPost(data: InfoPost)
}

class InfoPostOfflineRepository(private val dao: InfoPostDao):InfoPostRepository{
    override fun getAllInfoPostStream(): Flow<List<InfoPost>> = dao.getAllItems()

    override fun getInfoPostStream(serverId: String): Flow<InfoPost?> = dao.getItem(serverId)

    override suspend fun insertInfoPost(data: InfoPost) = dao.insert(data)

    override suspend fun insertMultipleInfoPost(data: List<InfoPost>, update: Boolean) {
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
            Log.d(GENERIC_TAG, "infoPost newData $newData updateData $updateData")
        }
    }

    override suspend fun deleteInfoPost(data: InfoPost) = dao.delete(data)

    override suspend fun updateInfoPost(data: InfoPost) = dao.update(data)

}