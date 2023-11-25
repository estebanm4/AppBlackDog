package com.dadm.appblackdog.viewmodels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dadm.appblackdog.LoginActivity
import com.dadm.appblackdog.MainActivity
import com.dadm.appblackdog.database.data.AgeRangeRepository
import com.dadm.appblackdog.services.FIREBASE_TAG
import com.dadm.appblackdog.services.FirebaseService
import com.dadm.appblackdog.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class SplashViewModel(
    private val ageRangeRepository: AgeRangeRepository,
    private val firebaseService: FirebaseService,
) : ViewModel() {


    fun init() {
        Log.d(FIREBASE_TAG, "init *************")
        viewModelScope.launch { loadDataFromServer() }
    }

    suspend fun loadDataFromServer() = coroutineScope {
        val ageRanges =
            async(Dispatchers.IO) { firebaseService.getData(Constants.AGE_RANGES_TABLE_NAME) }.await()
        Log.d(FIREBASE_TAG, "rangos de edad desde el servidor: ${ageRanges.size}")

    }

    fun navigateToLogin(context: Context) {
//        val context = LocalContext.current
        context.startActivity(Intent(context, LoginActivity::class.java))
        (context as Activity).finish()
    }

    fun navigateToDashboard(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
        (context as Activity).finish()
    }
}