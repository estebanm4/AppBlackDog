package com.dadm.appblackdog.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dadm.appblackdog.R
import com.dadm.appblackdog.ui.theme.AppBlackDogTheme
import com.dadm.appblackdog.ui_elements.MainAppBar

@Composable
fun UserDataScreen(navController: NavController, drawerState: DrawerState) {
    //content
    Scaffold(
        topBar = { MainAppBar(drawerState = drawerState) }
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            Text(text = "User data")
            Spacer(modifier = Modifier.padding(vertical = 5.dp))
            Image(
                painter = painterResource(id = R.drawable.owner_profile),

                contentDescription = "owner",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(
                        1.5.dp,
                        MaterialTheme.colorScheme.secondary,
                        CircleShape
                    )
            )
            Spacer(modifier = Modifier.padding(vertical = 5.dp))
            OutlinedButton(onClick = {}) {
                Text(text = stringResource(id = R.string.next_button))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val context = LocalContext.current
    AppBlackDogTheme {
        UserDataScreen(NavController(context), DrawerState(DrawerValue.Closed))
    }
}