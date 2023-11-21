package com.dadm.appblackdog

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dadm.appblackdog.screens.CalendarScreen
import com.dadm.appblackdog.screens.InfoScreen
import com.dadm.appblackdog.screens.MapScreen
import com.dadm.appblackdog.screens.RecipesScreen
import com.dadm.appblackdog.screens.UserDataScreen

enum class BlackDogsScreen() {
    UserData,
    Map,
    Recipes,
    Calendar,
    Info
}

@Composable
fun MainActivityScreen(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    Scaffold { padding ->
        NavHost(
            navController = navController,
            startDestination = BlackDogsScreen.UserData.name,
            modifier = modifier.padding(padding)
        ) {
            composable(BlackDogsScreen.UserData.name) {
                UserDataScreen(action = { navController.navigate(BlackDogsScreen.Map.name) })
            }
            composable(BlackDogsScreen.Map.name) {
                MapScreen(action = { navController.navigate(BlackDogsScreen.Recipes.name) })
            }
            composable(BlackDogsScreen.Recipes.name) {
                RecipesScreen(action = { navController.navigate(BlackDogsScreen.Calendar.name) })
            }
            composable(BlackDogsScreen.Calendar.name) {
                CalendarScreen(action = { navController.navigate(BlackDogsScreen.Info.name) })
            }
            composable(BlackDogsScreen.Info.name) {
                InfoScreen(action = {
                    navController.popBackStack(
                        BlackDogsScreen.UserData.name,
                        inclusive = false
                    )
                })
            }
        }

    }
}
