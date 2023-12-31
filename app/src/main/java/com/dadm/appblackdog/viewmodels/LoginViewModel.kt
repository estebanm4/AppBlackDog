package com.dadm.appblackdog.viewmodels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.dadm.appblackdog.MainActivity
import com.dadm.appblackdog.R
import com.dadm.appblackdog.objects.UiLogin
import com.dadm.appblackdog.services.FirebaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    private val firebaseService = FirebaseService()
    private val _uiState = MutableStateFlow(UiLogin())
    val uiState: StateFlow<UiLogin> = _uiState.asStateFlow()

    suspend fun firebaseLogin(context: Context) {
        _uiState.update { state -> state.copy(isLoaderEnable = true) }
        val success: Boolean
        if (validateFields()) {
            success =
                firebaseService.firebaseAuthentication(data = uiState.value, context = context)
            Log.d("firebase", "actualiza ui $success")
            if (success) {
                navigateToMainScreen(context)
                reset()
            } else {
                showLoginAlert(context)
                _uiState.update { state -> state.copy(isLoaderEnable = false) }
            }

        }
    }

    fun updateEmail(data: String) {
        _uiState.update { state -> state.copy(email = data) }
    }

    fun updatePassword(data: String) {
        _uiState.update { state -> state.copy(password = data) }
    }

    fun updateRememberMe() {
        _uiState.update { state -> state.copy(remember = !state.remember) }
    }

    private fun navigateToMainScreen(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
        (context as Activity).finish()
    }

    private fun showLoginAlert(context: Context) {
        Toast.makeText(
            context,
            ContextCompat.getString(context, R.string.server_error),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun validateFields(): Boolean {
        return uiState.value.email.isNotEmpty() && uiState.value.password.isNotEmpty()
    }

    private fun reset() {
        _uiState.update { currentState ->
            currentState.copy(
                email = "",
                password = "",
                isLoaderEnable = false
            )
        }
    }
}