package com.dadm.appblackdog

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dadm.appblackdog.models.BlackDogNavigationRoutes
import com.dadm.appblackdog.screens.AddPetScreen
import com.dadm.appblackdog.screens.CalendarScreen
import com.dadm.appblackdog.screens.InfoScreen
import com.dadm.appblackdog.screens.MapScreen
import com.dadm.appblackdog.screens.RecipesScreen
import com.dadm.appblackdog.screens.UserDataScreen
import com.dadm.appblackdog.ui_elements.CustomDrawer
import com.dadm.appblackdog.viewmodels.AppViewModelProvider
import com.dadm.appblackdog.viewmodels.InfoViewModel
import com.dadm.appblackdog.viewmodels.MainScreenViewModel
import com.dadm.appblackdog.viewmodels.PetAddViewModel
import com.dadm.appblackdog.viewmodels.PetProfileViewModel
import com.dadm.appblackdog.viewmodels.RecipeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityScreen(
    mainScreenViewModel: MainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    petViewModel: PetAddViewModel = viewModel(factory = AppViewModelProvider.Factory),
    recipeViewModel: RecipeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    profileViewModel: PetProfileViewModel = viewModel(factory = AppViewModelProvider.Factory),
    infoViewModel: InfoViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    when {
        mainScreenViewModel.loadData -> mainScreenViewModel.init()
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            CustomDrawer(
                navController = navController,
                navBackStackEntry = navBackStackEntry,
                drawerState = drawerState,
                mainScreenViewModel = mainScreenViewModel
            )
        }
    ) {
        Scaffold(
            modifier = modifier,
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = BlackDogNavigationRoutes.UserData.name,
                modifier = Modifier.padding(padding),
            ) {
                composable(BlackDogNavigationRoutes.UserData.name) {
                    UserDataScreen(
                        navController = navController,
                        drawerState = drawerState,
                        petViewModel = petViewModel,
                        profileViewModel = profileViewModel
                    )
                }
                composable(BlackDogNavigationRoutes.Map.name) {
                    MapScreen(navController = navController, drawerState = drawerState)
                }
                composable(BlackDogNavigationRoutes.Recipes.name) {
                    recipeViewModel.init()
                    RecipesScreen(
                        drawerState = drawerState,
                        recipeViewModel = recipeViewModel
                    )

                }
                composable(BlackDogNavigationRoutes.Calendar.name) {
                    CalendarScreen(action = { })
                }
                composable(BlackDogNavigationRoutes.Info.name) {
                    InfoScreen(drawerState,infoViewModel)
                }
                composable(BlackDogNavigationRoutes.AddPet.name) {
                    AddPetScreen(navController = navController, petViewModel = petViewModel)
                }
            }

        }
    }
}
