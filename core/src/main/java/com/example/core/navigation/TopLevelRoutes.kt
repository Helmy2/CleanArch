package com.example.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector


data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

object TopLevelRoutes {
    val routes = listOf(
        TopLevelRoute("Home", Destination.Home, Icons.Default.Home),
        TopLevelRoute("Profile", Destination.Profile, Icons.Default.AccountCircle),
    )
}