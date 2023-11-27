package com.dadm.appblackdog.database.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dadm.appblackdog.models.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Recipe)

    @Update
    suspend fun update(item: Recipe)

    @Delete
    suspend fun delete(item: Recipe)

    @Query("SELECT * from recipes WHERE serverId = :serverId")
    fun getItem(serverId: String): Flow<Recipe>

    @Query("SELECT * from recipes ORDER BY name")
    fun getAllItems(): Flow<List<Recipe>>

    @Query("SELECT COUNT() from recipes WHERE serverId = :serverId ")
    fun checkItemByServerId(serverId: String): Flow<Int>

    @Query("SELECT id from recipes WHERE serverId = :serverId ")
    fun getItemId(serverId: String): Flow<Int>
}