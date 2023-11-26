package com.dadm.appblackdog.models

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.graphics.vector.ImageVector
import com.dadm.appblackdog.R


sealed class NavigationButton(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    object DogProfile : NavigationButton(
        route = BlackDogNavigationRoutes.UserData.name,
        resourceId = R.string.my_pets,
        icon = Icons.Default.Pets
    )

    object Map : NavigationButton(
        route = BlackDogNavigationRoutes.Map.name,
        resourceId = R.string.map_label,
        icon = Icons.Default.Map
    )

    object Recipes : NavigationButton(
        route = BlackDogNavigationRoutes.Recipes.name,
        resourceId = R.string.recipes,
        icon = Icons.Default.Restaurant
    )

    object Calendar : NavigationButton(
        route = BlackDogNavigationRoutes.Calendar.name,
        resourceId = R.string.calendar,
        icon = Icons.Default.CalendarMonth
    )

    object Info : NavigationButton(
        route = BlackDogNavigationRoutes.Info.name,
        resourceId = R.string.more_info,
        icon = Icons.Default.Info
    )
}
