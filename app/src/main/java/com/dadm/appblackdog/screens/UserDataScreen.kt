package com.dadm.appblackdog.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dadm.appblackdog.R
import com.dadm.appblackdog.models.BlackDogNavigationRoutes
import com.dadm.appblackdog.ui.theme.AppBlackDogTheme
import com.dadm.appblackdog.ui_elements.MainAppBar
import com.dadm.appblackdog.viewmodels.PetAddViewModel
import com.dadm.appblackdog.viewmodels.PetProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun UserDataScreen(
    navController: NavController,
    drawerState: DrawerState,
    petViewModel: PetAddViewModel?,
    profileViewModel: PetProfileViewModel,
) {
    //content
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
//        if (profileViewModel.pets.isEmpty())
//            WithoutPetsScreen(navController, drawerState, petViewModel)
//        else
            PetProfileScreen(navController, drawerState, petViewModel, profileViewModel)
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val context = LocalContext.current
    AppBlackDogTheme {
//        UserDataScreen(
//            petViewModel = null,
//            profileViewModel = null,
//            navController = NavController(context),
//            drawerState = DrawerState(
//                DrawerValue.Closed
//            ),
//        )
    }
}

