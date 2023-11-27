package com.dadm.appblackdog.database.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dadm.appblackdog.models.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Reminder)

    @Update
    suspend fun update(item: Reminder)

    @Delete
    suspend fun delete(item: Reminder)

    @Query("SELECT * from reminders WHERE serverId = :serverId")
    fun getItem(serverId: String): Flow<Reminder>

    @Query("SELECT * from reminders ORDER BY name")
    fun getAllItems(): Flow<List<Reminder>>

    @Query("SELECT COUNT() from reminders WHERE serverId = :serverId ")
    fun checkItemByServerId(serverId: String): Flow<Int>

    @Query("SELECT id from reminders WHERE serverId = :serverId ")
    fun getItemId(serverId: String): Flow<Int>
}