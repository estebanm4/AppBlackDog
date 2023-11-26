package com.dadm.appblackdog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                    StartScreen()
                }
            }
        }
    }
}

enum class StartScreenUri {
    Login,
    Register,
}

@Composable
fun StartScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = StartScreenUri.Login.name,
    ) {
        composable(StartScreenUri.Login.name) {
            LoginFormScreen(
                actionNavigation = { navController.navigate(StartScreenUri.Register.name) }
            )
        }
        composable(StartScreenUri.Register.name) {
            RegisterScreen(
                navigationAction = {
                    navController.popBackStack(
                        StartScreenUri.Login.name,
                        inclusive = false
                    )
                }
            )
        }
    }
}
