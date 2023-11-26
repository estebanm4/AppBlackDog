package com.dadm.appblackdog.database.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dadm.appblackdog.models.MeasureUnit
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasureUnitDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: MeasureUnit)

    @Update
    suspend fun update(item: MeasureUnit)

    @Delete
    suspend fun delete(item: MeasureUnit)

    @Query("SELECT * from measureUnits WHERE type = :type")
    fun getItemList(type: String): Flow<List<MeasureUnit>>

    @Query("SELECT * from measureUnits ORDER BY type")
    fun getAllItems(): Flow<List<MeasureUnit>>
    @Query("SELECT COUNT() from measureUnits WHERE serverId = :serverId ")
    fun checkItemByServerId(serverId:String): Flow<Int>
    @Query("SELECT id from measureUnits WHERE serverId = :serverId ")
    fun getItemId(serverId:String): Flow<Int>
}