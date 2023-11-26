package com.dadm.appblackdog.ui_elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dadm.appblackdog.BlackDogsScreen
import com.dadm.appblackdog.R

@Composable
fun CustomDrawer(navController: NavController?) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .weight(2f)
        ) {
            Box(
                modifier = Modifier
                    .background(Color.LightGray)
                    .fillMaxSize()
                    .weight(1f)
            ) {

            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(4f)
                    .padding(16.dp)
            ) {
                DrawerButton(
                    label = stringResource(id = R.string.my_pets),
                    icon = Icons.Default.Pets,
                    onClick = {
                        navController?.popBackStack(
                            BlackDogsScreen.UserData.name,
                            inclusive = false
                        )
                    }
                )
                DrawerButton(
                    label = stringResource(id = R.string.map_label),
                    icon = Icons.Default.Map,
                    onClick = {navController?.popBackStack(
                        BlackDogsScreen.Map.name,
                        inclusive = false
                    )}
                )
                DrawerButton(
                    label = stringResource(id = R.string.recipes),
                    icon = Icons.Default.Restaurant,
                    onClick = {navController?.popBackStack(
                        BlackDogsScreen.Recipes.name,
                        inclusive = false
                    )}
                )
                DrawerButton(
                    label = stringResource(id = R.string.calendar),
                    icon = Icons.Default.CalendarMonth,
                    onClick = {navController?.popBackStack(
                        BlackDogsScreen.Calendar.name,
                        inclusive = false
                    )}
                )
                DrawerButton(
                    label = stringResource(id = R.string.more_info),
                    icon = Icons.Default.Info,
                    onClick = {navController?.popBackStack(
                        BlackDogsScreen.Info.name,
                        inclusive = false
                    )}
                )
            }
        }
        Box(modifier = Modifier.weight(1f)) {}
    }

}

@Preview
@Composable
fun CustomDrawerPreview() {
    CustomDrawer(navController = null)
}