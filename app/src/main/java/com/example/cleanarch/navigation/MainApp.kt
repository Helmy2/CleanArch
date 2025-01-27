package com.example.cleanarch.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.core.navigation.Destination
import com.example.core.navigation.Navigator
import com.example.core.snackbar.SnackbarManager
import com.example.core.theme.CleanArchTheme
import com.example.core.navigation.TopLevelRoutes
import org.koin.compose.getKoin


@Composable
fun MainApp() {
    val navigator = getKoin().get<Navigator>().also {
        it.navController = rememberNavController()
    }
    val snackbarManager = getKoin().get<SnackbarManager>().also {
        it.snackbarHostState = remember { SnackbarHostState() }
    }

    CleanArchTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarManager.snackbarHostState!!) },
            bottomBar = {
                val navBackStackEntry by navigator.navController!!.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination?.route
                val nameForCurrentDestination = currentDestination?.substringAfterLast(".")

                AnimatedVisibility(
                    visible = TopLevelRoutes.routes.any { it.name == nameForCurrentDestination },
                ) {
                    BottomNavBar(
                        onClick = {
                            navigator.navigateToTopLevelRute(it)
                        },
                        currentRoute = nameForCurrentDestination ?: ""
                    )
                }
            },
        ) { innerPadding ->
            MainNavigation(
                navController = navigator.navController!!,
                startDestination = Destination.Auth,
                modifier = Modifier
                    .padding(innerPadding)
            )
        }
    }
}



