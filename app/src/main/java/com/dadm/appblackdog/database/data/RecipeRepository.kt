package com.dadm.appblackdog.database.data

import android.util.Log
import com.dadm.appblackdog.models.Recipe
import com.dadm.appblackdog.services.GENERIC_TAG
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

interface RecipeRepository {
    fun getAllRecipeStream(): Flow<List<Recipe>>

    fun getRecipeStream(serverId: String): Flow<Recipe?>

    suspend fun insertRecipe(data: Recipe)
    suspend fun insertMultipleRecipe(data: List<Recipe>, update: Boolean = true)

    suspend fun deleteRecipe(data: Recipe)

    suspend fun updateRecipe(data: Recipe)
}

class RecipeOfflineRepository(private val dao: RecipeDao): RecipeRepository{
    override fun getAllRecipeStream(): Flow<List<Recipe>> = dao.getAllItems()

    override fun getRecipeStream(serverId: String): Flow<Recipe?> = dao.getItem(serverId)

    override suspend fun insertRecipe(data: Recipe) = dao.insert(data)

    override suspend fun insertMultipleRecipe(data: List<Recipe>, update: Boolean) {
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
            Log.d(GENERIC_TAG, "recipes newData $newData updateData $updateData")
        }
    }

    override suspend fun deleteRecipe(data: Recipe) = dao.delete(data)

    override suspend fun updateRecipe(data: Recipe) = dao.update(data)
}