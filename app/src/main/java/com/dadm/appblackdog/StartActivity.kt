package com.dadm.appblackdog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dadm.appblackdog.screens.SplashScreen
import com.dadm.appblackdog.ui.theme.AppBlackDogTheme
import kotlinx.coroutines.coroutineScope

class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppBlackDogTheme {
                SplashScreen()
            }
        }
    }
}

