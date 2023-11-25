package com.dadm.appblackdog.database.data

import android.util.Log
import com.dadm.appblackdog.models.AgeRange
import com.dadm.appblackdog.services.FIREBASE_TAG
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
    override suspend fun insertMultipleAgeRange(data: List<AgeRange>) {
        var newData = 0
        var updateData = 0
        coroutineScope {

            if (data.isNotEmpty())
                data.forEach() {
                    val count = async { dao.checkItemByServerId(it.serverId).first() }.await()
                    if (count > 0) {
                        val id = async { dao.getItemId(it.serverId).first() }.await()
                        val item = it.copy(id = id)
                        launch { dao.update(item) }
                        updateData++
                    } else {
                        launch { dao.insert(it) }
                        newData++
                    }
                }
            Log.d(FIREBASE_TAG, "newData $newData updateData $updateData")
            val testData = async { dao.getItem(1).first() }.await()
            Log.d(FIREBASE_TAG, "test data: description ${testData.description} max ${testData.max}")
        }
    }

    /** delete item*/
    override suspend fun deleteAgeRange(data: AgeRange) = dao.delete(data)

    /** update item*/
    override suspend fun updateAgeRange(data: AgeRange) = dao.update(data)
}