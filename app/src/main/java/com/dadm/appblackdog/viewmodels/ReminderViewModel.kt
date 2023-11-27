package com.dadm.appblackdog.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dadm.appblackdog.database.data.ReminderRepository
import com.dadm.appblackdog.models.UiReminder
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
import java.time.Instant
import java.util.Calendar

class ReminderViewModel(
    private val reminderRepository: ReminderRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiReminder())
    val uiState: StateFlow<UiReminder> = _uiState.asStateFlow()

    fun loadReminders(){
        Log.d(GENERIC_TAG,"cargando recordatorios desde base de datos ****************")
        viewModelScope.launch { loadData() }
    }
    private suspend fun loadData() = coroutineScope {
        val dbPost = async(Dispatchers.IO) { reminderRepository.getAllReminderStream() }.await()
        val list = dbPost.first().sortedBy { it.time }
        _uiState.update { it.copy(items = list) }
    }

    fun calculateCalendarDays(){


    }
}