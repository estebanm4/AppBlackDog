package com.dadm.appblackdog.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dadm.appblackdog.R
import com.dadm.appblackdog.database.data.OwnerRepository
import com.dadm.appblackdog.models.Owner
import com.dadm.appblackdog.models.UiRegister
import com.dadm.appblackdog.services.FirebaseService
import com.dadm.appblackdog.services.GENERIC_TAG
import com.dadm.appblackdog.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val ownerRepository: OwnerRepository,
    private val firebaseService: FirebaseService,
) : ViewModel() {
    //variable
    private val _uiState = MutableStateFlow(UiRegister())
    val uiState: StateFlow<UiRegister> = _uiState.asStateFlow()

    fun resetData(){
        _uiState.value = UiRegister()
    }
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

    private fun showLoader(show: Boolean) {
        _uiState.update { it.copy(isLoaderEnable = show) }
    }

    fun validateForm(context: Context) {
        showLoader(true)
        if (_uiState.value.validateForm()) {
            viewModelScope.launch { createUser(context) }
        } else {
            _uiState.update {
                it.copy(
                    emailError = !it.validateEmail(),
                    passwordError = it.password.length < 6,
                    validatePasswordError = (it.password != it.validatedPassword) || it.validatedPassword.isEmpty(),
                    nameError = it.name.isEmpty(),
                    lastnameError = it.lastname.isEmpty(),
                )
            }
            showLoader(false)
        }
    }

    private suspend fun createUser(context: Context) = coroutineScope {
        //variables
        val newUser = _uiState.value.toOwnerMap()
        // create a account in firebase Auth
        val successAuth = firebaseService.addNewUser(
            email = _uiState.value.email,
            password = _uiState.value.password,
        )
        Log.d(GENERIC_TAG, "successAuth? ${successAuth != null}")

        // after, create a owner with user data in firestore
        if (successAuth != null) {
            val successCreate = async(Dispatchers.IO) {
                firebaseService.setData(reference = Constants.OWNER_TABLE_NAME, data = newUser)
            }.await()
            // when complete server data task, start process to save user data in db
            if (successCreate) {
                // load all data data from owner table in firestore (required for obtain serverId)
                val user = async(Dispatchers.IO) {
                    firebaseService.getDataByArgument(
                        reference = Constants.OWNER_TABLE_NAME,
                        argument = "email",
                        value = _uiState.value.email
                    )
                }.await()
                // finally save the data in local db and finish user register
                if (!user.isNullOrEmpty()) {
                    val loginUser = user.first()
                    val ownerDb = Owner(
                        serverId = loginUser.id,
                        name = loginUser.data["name"] as String,
                        lastname = loginUser.data["lastname"] as String,
                        email = loginUser.data["email"] as String,
                        hasPets = loginUser.data["hasPets"] as Boolean,
                        photoUrl = loginUser.data["photoUrl"] as String,
                    )
                    // clean owner from last session
                    ownerRepository.cleanOwners()
                    // insert new owner data
                    ownerRepository.insertOwner(ownerDb)
                    Log.d(GENERIC_TAG, "Register process end ************")
                }
            }
        } else {
            Toast.makeText(
                context,
                ContextCompat.getString(context, R.string.create_user_error),
                Toast.LENGTH_SHORT
            ).show()
        }
        showLoader(false)
    }
}