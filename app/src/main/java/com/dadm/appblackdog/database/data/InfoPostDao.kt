package com.dadm.appblackdog.database.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dadm.appblackdog.models.InfoPost
import kotlinx.coroutines.flow.Flow
@Dao
interface InfoPostDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: InfoPost)

    @Update
    suspend fun update(item: InfoPost)

    @Delete
    suspend fun delete(item: InfoPost)

    @Query("SELECT * from infoPosts WHERE serverId = :serverId")
    fun getItem(serverId: String): Flow<InfoPost>

    @Query("SELECT * from infoPosts ORDER BY name")
    fun getAllItems(): Flow<List<InfoPost>>

    @Query("SELECT COUNT() from infoPosts WHERE serverId = :serverId ")
    fun checkItemByServerId(serverId: String): Flow<Int>

    @Query("SELECT id from infoPosts WHERE serverId = :serverId ")
    fun getItemId(serverId: String): Flow<Int>
}