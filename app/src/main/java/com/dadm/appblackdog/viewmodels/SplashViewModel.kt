package com.dadm.appblackdog.viewmodels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dadm.appblackdog.LoginActivity
import com.dadm.appblackdog.MainActivity
import com.dadm.appblackdog.database.data.OwnerRepository
import com.dadm.appblackdog.models.UiSplash
import com.dadm.appblackdog.services.FirebaseService
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

class SplashViewModel(
    private val ownerRepository: OwnerRepository,
    private val firebaseService: FirebaseService,
) : ViewModel() {

    private var loadIsRun = false
    private val _uiState = MutableStateFlow(UiSplash())
    val uiState: StateFlow<UiSplash> = _uiState.asStateFlow()

    fun init() {
        if (!loadIsRun) {
            Log.d(GENERIC_TAG, "init *************")
            loadIsRun = true
            _uiState.update { ui -> ui.copy(navigate = false) }
            viewModelScope.launch { loadDataFromServer() }
        }
    }

    /** load common data from server*/
    private suspend fun loadDataFromServer() = coroutineScope {
        Log.d(GENERIC_TAG, "load data *************")
        launch(Dispatchers.IO) { firebaseService.init() }
        val users = async(Dispatchers.IO) { ownerRepository.getOwnerStream().first() }.await()
        _uiState.update { ui -> ui.copy(navigate = true, isUserLogin = users.isNotEmpty()) }
//        withContext(Dispatchers.Main) {
//
//        }
    }

    fun navigateToScreen(context: Context) {
        Log.d(GENERIC_TAG, "start navigation *********************")
        if (_uiState.value.isUserLogin) navigateToDashboard(context)
        else navigateToLogin(context)
        loadIsRun = false
    }

    private fun navigateToLogin(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java))
        (context as Activity).finish()
    }

    private fun navigateToDashboard(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
        (context as Activity).finish()
    }
}