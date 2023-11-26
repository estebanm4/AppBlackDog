package com.dadm.appblackdog.database.data

import android.util.Log
import com.dadm.appblackdog.models.MeasureUnit
import com.dadm.appblackdog.services.GENERIC_TAG
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

interface MeasureUnitRepository {
    fun getAllMeasureUnitStream(): Flow<List<MeasureUnit>>

    fun getMeasureUnitStream(value: String): Flow<List<MeasureUnit>>

    suspend fun insertMeasureUnit(data: MeasureUnit)
    suspend fun insertMultipleMeasureUnit(data: List<MeasureUnit>, update: Boolean = true)

    suspend fun deleteMeasureUnit(data: MeasureUnit)

    suspend fun updateMeasureUnit(data: MeasureUnit)
}

class MeasureUnitOfflineRepository(private val dao: MeasureUnitDao) : MeasureUnitRepository {
    override fun getAllMeasureUnitStream(): Flow<List<MeasureUnit>> = dao.getAllItems()

    override fun getMeasureUnitStream(value: String): Flow<List<MeasureUnit>> = dao.getItemList(value)

    override suspend fun insertMeasureUnit(data: MeasureUnit) = dao.insert(data)

    override suspend fun insertMultipleMeasureUnit(data: List<MeasureUnit>, update: Boolean) {
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

    override suspend fun deleteMeasureUnit(data: MeasureUnit) = dao.delete(data)

    override suspend fun updateMeasureUnit(data: MeasureUnit) = dao.update(data)


}