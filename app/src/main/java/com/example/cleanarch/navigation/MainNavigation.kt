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
import com.example.core.navigation.Destination
import com.example.feature.auth.AuthRoute
import com.example.feature.home.presentation.HomeRoute

@Composable
fun MainNavigation(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable<Destination.Home> {
            HomeRoute()
        }

        composable<Destination.Details> {
            val details = it.toRoute<Destination.Details>()
            Box(Modifier.fillMaxSize()) {
                Text("Details: ${details.id}")
            }
        }

        composable<Destination.Profile> {
            Box(Modifier.fillMaxSize()) {
                Text("Profile")
            }
        }

        composable<Destination.Auth> {
            AuthRoute()
        }
    }
}

