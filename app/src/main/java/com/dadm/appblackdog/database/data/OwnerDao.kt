package com.dadm.appblackdog.database.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dadm.appblackdog.models.Owner
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Owner)

    @Update
    suspend fun update(item: Owner)

    @Delete
    suspend fun delete(item: Owner)

    @Query("SELECT * from owners WHERE serverId = :serverId")
    fun getItem(serverId: String): Flow<Owner>
}