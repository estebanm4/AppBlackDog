package com.dadm.appblackdog.database.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dadm.appblackdog.models.Breed
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Breed)

    @Update
    suspend fun update(item: Breed)

    @Delete
    suspend fun delete(item: Breed)

    @Query("SELECT * from breeds WHERE id = :id")
    fun getItem(id: Int): Flow<Breed>

    @Query("SELECT * from breeds ORDER BY name")
    fun getAllItems(): Flow<List<Breed>>

    @Query("SELECT COUNT() from breeds WHERE serverId = :serverId ")
    fun checkItemByServerId(serverId: String): Flow<Int>

    @Query("SELECT id from breeds WHERE serverId = :serverId ")
    fun getItemId(serverId: String): Flow<Int>
}