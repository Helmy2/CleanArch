package com.example.cleanarch.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.core.navigation.AppDestination
import com.example.core.navigation.NavControllerProvider
import com.example.core.snackbar.SnackbarHostStateProvider
import com.example.core.theme.CleanArchTheme
import org.koin.compose.getKoin


data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

val topLevelRoutes =
    listOf(
        TopLevelRoute("Home", AppDestination.Home, Icons.Default.Home),
        TopLevelRoute(
            "Profile", AppDestination.Profile, Icons.Default.AccountCircle
        ),
    )

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    getKoin().get<NavControllerProvider>().also {
        it.navController = navController
    }
    getKoin().get<SnackbarHostStateProvider>().also {
        it.snackbarHostState = snackbarHostState
    }

    CleanArchTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                AnimatedVisibility(
                    visible = topLevelRoutes.any { it.route.hashCode() == currentDestination?.id },
                ) {
                    NavigationBar {
                        topLevelRoutes.forEach { topLevelRoute ->
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        topLevelRoute.icon,
                                        contentDescription = topLevelRoute.name
                                    )
                                },
                                label = { Text(topLevelRoute.name) },
                                selected = currentDestination?.id == topLevelRoute.route.hashCode(),
                                onClick = {
                                    navController.navigate(topLevelRoute.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                            )
                        }
                    }
                }
            },
        ) { innerPadding ->
            MainNavigation(
                navController = navController,
                startDestination = AppDestination.Auth,
                modifier = Modifier
                    .padding(innerPadding)
            )
        }
    }
}



