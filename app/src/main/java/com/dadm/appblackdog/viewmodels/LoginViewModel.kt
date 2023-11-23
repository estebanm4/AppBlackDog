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
import com.dadm.appblackdog.models.Pet
import com.dadm.appblackdog.models.UiLogin
import com.dadm.appblackdog.services.FirebaseService
import com.dadm.appblackdog.utils.Constants
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

class LoginViewModel : ViewModel() {
    private val firebaseService = FirebaseService()
    private val _uiState = MutableStateFlow(UiLogin())
    val uiState: StateFlow<UiLogin> = _uiState.asStateFlow()

    suspend fun firebaseLogin(context: Context) {
        _uiState.update { state -> state.copy(isLoaderEnable = true) }
        val success: Boolean
        if (validateFields()) {
            success =
                firebaseService.emailLogin(data = uiState.value, context = context)
            Log.d("firebase", "actualiza ui $success")
            if (success) {
//                navigateToMainScreen(context)
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

    /** firestore test  */
    val newPet = Pet(
        name = "Zeus",
        ownerId = "PJBR1Iv9bAynz8BPrZN7",
        photoUrl = "https://static.wikia.nocookie.net/gintama/images/4/48/Sadaharu_mug.jpg/revision/latest?cb=20110921172540&path-prefix=es",
        ageRangeId = "KmzEyURfDZUUt20Em98P",
        description = "Perro entrenado para la vigilancia",
        raceId = "ogqf0kmEScHtnkyPITal",
        weight = 20f,
        measureUnitId = "iJL1wiaVcdokjckQFCRM",
        birthdate = Timestamp(Date(1353707011000))
    )

    suspend fun getData() {
        firebaseService.getData(Constants.PET_TABLE_NAME)
    }

    suspend fun sendData() {

        firebaseService.setData(reference = Constants.PET_TABLE_NAME, data = newPet)
    }

    suspend fun updateData() {
        firebaseService.updateData(
            reference = Constants.PET_TABLE_NAME,
            itemId = "YYxT2q1VTY2bmZKuHutg",
            argument = "description",
            value = "perro para cuidado de niños"
        )
    }

    suspend fun getDataByArgument() {
        firebaseService.getDataByArgument(
            reference = Constants.MEASURE_UNIT_TABLE_NAME,
            argument = "type",
            value = "peso"
        )
        firebaseService.userLogOut()
    }


}