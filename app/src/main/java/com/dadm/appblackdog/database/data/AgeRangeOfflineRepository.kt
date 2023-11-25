package com.dadm.appblackdog.database.data

import android.util.Log
import com.dadm.appblackdog.models.AgeRange
import com.dadm.appblackdog.services.GENERIC_TAG
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AgeRangeOfflineRepository(private val dao: AgeRangeDao) : AgeRangeRepository {
    /** get all items in table */
    override fun getAllAgeRangeStream(): Flow<List<AgeRange>> = dao.getAllItems()

    /** get an item from table*/
    override fun getAgeRangeStream(id: Int): Flow<AgeRange?> = dao.getItem(id)

    /** add one item*/
    override suspend fun insertAgeRange(data: AgeRange) = dao.insert(data)

    /** add multiple items*/
    override suspend fun insertMultipleAgeRange(data: List<AgeRange>, update: Boolean) {
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

    /** delete item*/
    override suspend fun deleteAgeRange(data: AgeRange) = dao.delete(data)

    /** update item*/
    override suspend fun updateAgeRange(data: AgeRange) = dao.update(data)
}