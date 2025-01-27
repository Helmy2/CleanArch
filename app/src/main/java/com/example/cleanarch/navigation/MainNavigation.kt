package com.example.cleanarch.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.core.navigation.AppDestination
import com.example.feature.auth.AuthRoute
import com.example.feature.home.presentation.HomeRoute

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
            Box(Modifier.fillMaxSize()) {
                Text("Details: ${details.id}")
            }
        }

        composable<AppDestination.Profile> {
            Box(Modifier.fillMaxSize()) {
                Text("Profile")
            }
        }

        composable<AppDestination.Auth> {
            AuthRoute()
        }
    }
}

