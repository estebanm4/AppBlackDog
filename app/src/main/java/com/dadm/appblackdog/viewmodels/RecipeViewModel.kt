package com.dadm.appblackdog.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dadm.appblackdog.database.data.RecipeRepository
import com.dadm.appblackdog.models.Recipe
import com.dadm.appblackdog.models.UiRecipeView
import com.dadm.appblackdog.services.GENERIC_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val recipeRepository: RecipeRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiRecipeView())
    val uiState: StateFlow<UiRecipeView> = _uiState.asStateFlow()

    private var recipes: List<Recipe> = listOf()
    fun init() {
        showLoader(true)
//        if (recipes.isEmpty())
            viewModelScope.launch { getData() }
//        else updateUi(recipes.random())
    }

    private suspend fun getData() = coroutineScope {
        val data = async(Dispatchers.IO) { recipeRepository.getAllRecipeStream() }.await()
        Log.d(GENERIC_TAG, "recetas recibidas de base de datos ${data.first().size}")
        if (data.first().isNotEmpty()) {
            recipes = data.first()
            updateUi(recipes.random())
        }
    }

    private fun updateUi(data: Recipe) {
        val items = data.items.split(",")
        _uiState.update { ui ->
            ui.copy(
                name = data.name,
                description = data.description,
                imageUrl = data.imageUrl,
                items = items,
            )
        }
        showLoader(false)
    }

    private fun showLoader(show: Boolean) {
        _uiState.update { ui -> ui.copy(loader = show) }
    }

    fun reloadRecipe() {
        showLoader(true)
        val list = recipes.filter { it.name != _uiState.value.name }
        if (list.isNotEmpty()) updateUi(list.random())
    }

}