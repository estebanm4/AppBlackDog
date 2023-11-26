package com.dadm.appblackdog.viewmodels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dadm.appblackdog.MainActivity
import com.dadm.appblackdog.R
import com.dadm.appblackdog.database.data.OwnerRepository
import com.dadm.appblackdog.models.Owner
import com.dadm.appblackdog.models.UiLogin
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

class LoginViewModel(
    private val ownerRepository: OwnerRepository,
    private val firebaseService: FirebaseService,
) : ViewModel() {
    //variables
    private val _uiState = MutableStateFlow(UiLogin())
    val uiState: StateFlow<UiLogin> = _uiState.asStateFlow()


    fun startLogin(context: Context) {
        //if form is valid start login
        if (validateFields()) {
            // enabled loader
            showLoader(true)
            // start background process
            viewModelScope.launch { loginProcess(context) }

        }
    }

    /** start user login*/
    private suspend fun loginProcess(context: Context) = coroutineScope {
        // auth firebase
        val firebaseJob = async(Dispatchers.IO) {
            firebaseService.emailLogin(data = uiState.value)
        }.await()

        // update UI
        if (firebaseJob) {
            // load all data data from owner table in firestore (required for obtain serverId)
            val user = async(Dispatchers.IO) {
                firebaseService.getDataByArgument(
                    reference = Constants.OWNER_TABLE_NAME,
                    argument = "email",
                    value = _uiState.value.email
                )
            }.await()
            Log.d(GENERIC_TAG, "load user info ${user?.isNotEmpty()}")
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
                Log.d(GENERIC_TAG, "Login process end ************")
                // when all task is ok, start navigation to main screen
                navigateToMainScreen(context)
                // reset data in viewModel
                reset()
            }
        } else {
            // Notify the user the cause of login error
            showLoginAlert(context)
            // disabled loader
            showLoader(false)
        }
    }

    fun updateEmail(data: String) {
        _uiState.update { state -> state.copy(email = data) }
    }

    fun updatePassword(data: String) {
        _uiState.update { state -> state.copy(password = data) }
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

    private fun showLoader(show: Boolean) {
        _uiState.update { state -> state.copy(isLoaderEnable = show) }
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