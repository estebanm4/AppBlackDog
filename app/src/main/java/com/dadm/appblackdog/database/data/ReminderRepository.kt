package com.dadm.appblackdog.database.data

import android.util.Log
import com.dadm.appblackdog.models.Reminder
import com.dadm.appblackdog.services.GENERIC_TAG
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

interface ReminderRepository {

    fun getAllReminderStream(): Flow<List<Reminder>>

    fun getReminderStream(serverId: String): Flow<Reminder?>

    suspend fun insertReminder(data: Reminder)
    suspend fun insertMultipleReminder(data: List<Reminder>, update: Boolean = true)

    suspend fun deleteReminder(data: Reminder)

    suspend fun updateReminder(data: Reminder)
}

class ReminderOfflineRepository(private val dao: ReminderDao) : ReminderRepository {
    override fun getAllReminderStream(): Flow<List<Reminder>> = dao.getAllItems()

    override fun getReminderStream(serverId: String): Flow<Reminder?> = dao.getItem(serverId)

    override suspend fun insertReminder(data: Reminder) = dao.insert(data)

    override suspend fun insertMultipleReminder(data: List<Reminder>, update: Boolean) {
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
            Log.d(GENERIC_TAG, "reminder newData $newData updateData $updateData")
        }
    }

    override suspend fun deleteReminder(data: Reminder) = dao.delete(data)

    override suspend fun updateReminder(data: Reminder) = dao.update(data)

}