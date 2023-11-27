package com.dadm.appblackdog.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dadm.appblackdog.database.data.InfoPostRepository
import com.dadm.appblackdog.models.UiInfoPost
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

class InfoViewModel(
    private val infoPostRepository: InfoPostRepository,
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(UiInfoPost())
    val uiState: StateFlow<UiInfoPost> = _uiState.asStateFlow()

    fun loadPost(){
        Log.d(GENERIC_TAG,"cargando post desde base de datos ****************")
        viewModelScope.launch { loadData() }
    }
    private suspend fun loadData() = coroutineScope {
        val dbPost = async(Dispatchers.IO) { infoPostRepository.getAllInfoPostStream() }.await()
        _uiState.update { it.copy(items = dbPost.first()) }
    }
}