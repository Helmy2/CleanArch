package com.example.cleanarch.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.cleanarch.presentation.details.DetailsRoute
import com.example.cleanarch.presentation.home.HomeRoute
import com.example.cleanarch.presentation.profile.ProfileRoute
import kotlinx.serialization.Serializable

@Composable
fun MainNavigation(
    navController: NavHostController,
    startDestination: AppDestination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable<AppDestination.Home> {
            HomeRoute(onNavigateToDetails = { navController.navigate(AppDestination.Details(it)) })
        }

        composable<AppDestination.Details> {
            val details = it.toRoute<AppDestination.Details>()
            DetailsRoute(id = details.id)
        }

        composable<AppDestination.Profile> {
            ProfileRoute()
        }
    }
}

sealed class AppDestination {
    @Serializable
    data object Home : AppDestination()

    @Serializable
    data object Profile : AppDestination()

    @Serializable
    data class Details(val id: Int) : AppDestination()
}