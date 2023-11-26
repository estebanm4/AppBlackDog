package com.dadm.appblackdog.database.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dadm.appblackdog.models.Pet
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Pet)

    @Update
    suspend fun update(item: Pet)

    @Delete
    suspend fun delete(item: Pet)

    @Query("SELECT * from pets WHERE serverId = :serverId")
    fun getItem(serverId: String): Flow<Pet>

    @Query("SELECT * from pets ORDER BY name")
    fun getAllItems(): Flow<List<Pet>>

    @Query("SELECT COUNT() from pets WHERE serverId = :serverId ")
    fun checkItemByServerId(serverId: String): Flow<Int>

    @Query("SELECT id from pets WHERE serverId = :serverId ")
    fun getItemId(serverId: String): Flow<Int>
}