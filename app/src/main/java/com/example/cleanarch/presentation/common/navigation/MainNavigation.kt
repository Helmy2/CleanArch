package com.example.cleanarch.presentation.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.cleanarch.presentation.auth.AuthRoute
import com.example.cleanarch.presentation.details.DetailsRoute
import com.example.cleanarch.presentation.home.HomeRoute
import com.example.cleanarch.presentation.profile.ProfileRoute

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
            HomeRoute()
        }

        composable<AppDestination.Details> {
            val details = it.toRoute<AppDestination.Details>()
            DetailsRoute(id = details.id)
        }

        composable<AppDestination.Profile> {
            ProfileRoute()
        }

        composable<AppDestination.Auth> {
            AuthRoute()
        }
    }
}

