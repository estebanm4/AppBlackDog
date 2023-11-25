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
import com.dadm.appblackdog.models.AgeRange
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

    /** load common data from server*/
    private suspend fun loadDataFromServer() = coroutineScope {
        //variables
        val ageRangeList = mutableListOf<AgeRange>()
        // get age ranges
        val ageRanges =
            async(Dispatchers.IO) { firebaseService.getData(Constants.AGE_RANGES_TABLE_NAME) }.await()
        Log.d(FIREBASE_TAG, "rangos de edad desde el servidor: ${ageRanges.size}")

        // when server return data generate a valid list to send to db
        if (ageRanges.isNotEmpty())
            ageRanges.map {
                val data = it.data
                ageRangeList.add(
                    AgeRange(
                        serverId = it.id,
                        max = (it.data["max"] as Long? ?: 0).toInt(),
                        min = (it.data["min"] as Long? ?: 0).toInt(),
                        description = it.data["description"] as String? ?: "",
                    )
                )
            }
        // save agesRanges in db
        if (ageRangeList.isNotEmpty()) {
            val save = async { ageRangeRepository.insertMultipleAgeRange(ageRangeList) }
            Log.d(FIREBASE_TAG, "Age ranged saved: ${ageRangeList.size}")
        }

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