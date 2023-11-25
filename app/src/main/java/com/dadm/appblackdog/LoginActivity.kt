package com.dadm.appblackdog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dadm.appblackdog.screens.LoginFormScreen
import com.dadm.appblackdog.screens.RegisterScreen
import com.dadm.appblackdog.ui.theme.AppBlackDogTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppBlackDogTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    LoginFormScreen()
                    RegisterScreen()
                }
            }
        }
    }
}

