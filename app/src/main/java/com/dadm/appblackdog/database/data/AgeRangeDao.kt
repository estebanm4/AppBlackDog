package com.dadm.appblackdog.database.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dadm.appblackdog.models.AgeRange
import kotlinx.coroutines.flow.Flow

@Dao
interface AgeRangeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: AgeRange)

    @Update
    suspend fun update(item: AgeRange)

    @Delete
    suspend fun delete(item: AgeRange)

    @Query("SELECT * from ageRanges WHERE id = :id")
    fun getItem(id: Int): Flow<AgeRange>

    @Query("SELECT * from ageRanges ORDER BY max ASC")
    fun getAllItems(): Flow<List<AgeRange>>
    @Query("SELECT COUNT() from ageRanges WHERE serverId = :serverId ")
    fun checkItemByServerId(serverId:String): Flow<Int>
    @Query("SELECT id from ageRanges WHERE serverId = :serverId ")
    fun getItemId(serverId:String): Flow<Int>
}