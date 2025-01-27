package com.example.core.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController


class NavigatorImpl : Navigator {
    override var navController: NavHostController? = null

    override fun navigateToDetails(id: String) {
        navController?.navigate(Destination.Details(id = id))
    }

    override fun navigateToAuth() {
        navController?.navigate(Destination.Auth)
    }

    override fun navigateToHome() {
        navController?.navigate(Destination.Home)
    }

    override fun navigateBack() {
        navController?.popBackStack()
    }

    override fun navigateToTopLevelRute(route: Destination) {
        navController?.apply {
            navigate(route) {
                popUpTo(graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
}