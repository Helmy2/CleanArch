package com.example.cleanarch.presentation.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cleanarch.presentation.details.DetailsRoute
import com.example.cleanarch.presentation.home.HomeRoute
import kotlinx.serialization.Serializable

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppDestination.Home) {
        composable<AppDestination.Home> {
            HomeRoute(onNavigateToDetails = { navController.navigate(AppDestination.Details(it)) })
        }

        composable<AppDestination.Details> {
            DetailsRoute(id = 1)
        }
    }
}

sealed class AppDestination {
    @Serializable
    data object Home : AppDestination()

    @Serializable
    data class Details(val id: Int) : AppDestination()
}