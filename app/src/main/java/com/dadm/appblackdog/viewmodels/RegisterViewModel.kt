package com.dadm.appblackdog.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.dadm.appblackdog.database.data.OwnerRepository
import com.dadm.appblackdog.models.UiRegister
import com.dadm.appblackdog.services.FirebaseService
import com.dadm.appblackdog.services.GENERIC_TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel(
    private val ownerRepository: OwnerRepository?,
    private val firebaseService: FirebaseService?,
) : ViewModel() {
    //variable
    private val _uiState = MutableStateFlow(UiRegister())
    val uiState: StateFlow<UiRegister> = _uiState.asStateFlow()

    fun updateName(data: String) {
        _uiState.update { state -> state.copy(name = data, nameError = data.isEmpty()) }
    }

    fun updateLastname(data: String) {
        _uiState.update { state -> state.copy(lastname = data, lastnameError = data.isEmpty()) }
    }

    fun updateEmail(data: String) {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val error = data.isEmpty() || !data.matches(Regex(emailPattern))
//        Log.d(GENERIC_TAG, "email error $error ${data.length}")
        _uiState.update { state -> state.copy(email = data, emailError = error) }
    }

    fun updatePassword(data: String) {
        val error = data.isEmpty() || data.length < 6
        _uiState.update { state ->
            state.copy(
                password = data,
                passwordError = error
            )
        }
    }

    fun updateValidatePassword(data: String) {
        _uiState.update { state ->
            state.copy(
                validatedPassword = data,
                validatePasswordError = (data.isEmpty() || data != state.password)
            )
        }
    }

    fun validateForm() {

        if (_uiState.value.validateForm()) createUser()
        else {
            _uiState.update { it ->
                it.copy(
                    emailError = !it.validateEmail(),
                    passwordError = it.password.length < 6,
                    validatePasswordError = (it.password != it.validatedPassword) || it.validatedPassword.isEmpty(),
                    nameError = it.name.isEmpty(),
                    lastnameError = it.lastname.isEmpty(),
                )
            }
        }
    }

    fun createUser() {}
}