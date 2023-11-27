package com.dadm.appblackdog.ui_elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.dadm.appblackdog.R
import com.dadm.appblackdog.models.NavigationButton
import com.dadm.appblackdog.viewmodels.MainScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun CustomDrawer(
    navBackStackEntry: NavBackStackEntry?,
    navController: NavController,
    drawerState: DrawerState,
    mainScreenViewModel: MainScreenViewModel?
) {
    val currentDestination = navBackStackEntry?.destination
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val items = listOf(
        NavigationButton.DogProfile,
        NavigationButton.Map,
        NavigationButton.Recipes,
        NavigationButton.Calendar,
        NavigationButton.Info
    )
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
            /** menu items */
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(4f)
                    .padding(16.dp)
            ) {
                items.forEach {
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = it.icon,
                                contentDescription = stringResource(id = R.string.default_description),
                                tint = if (currentDestination?.hierarchy?.any { destination ->
                                        destination.route == it.route
                                    } == true)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(32.dp)
                            )
                        },
                        label = { Text(text = stringResource(it.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { destination ->
                            destination.route == it.route
                        } == true,
                        onClick = {
                            navController.navigate(it.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        }
                    )
                }

            }
            /** bottom menu */
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(16.dp)
            ) {
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = stringResource(id = R.string.default_description),
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(32.dp)
                        )
                    },
                    label = { Text(text = stringResource(R.string.log_out)) },
                    selected = false,
                    onClick = { mainScreenViewModel?.logOut(context) })
            }
        }
        Box(modifier = Modifier.weight(1f)) {}
    }

}

@Preview
@Composable
fun CustomDrawerPreview() {
    val context = LocalContext.current
    CustomDrawer(null, NavController(context), DrawerState(DrawerValue.Closed), null)
}