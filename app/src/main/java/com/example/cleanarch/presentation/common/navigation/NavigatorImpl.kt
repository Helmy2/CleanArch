package com.example.cleanarch.presentation.common.navigation

import com.example.cleanarch.di.NavControllerProvider
import kotlinx.serialization.Serializable


sealed class AppDestination {
    @Serializable
    data object Home : AppDestination()

    @Serializable
    data object Auth : AppDestination()

    @Serializable
    data object Profile : AppDestination()

    @Serializable
    data class Details(val id: String) : AppDestination()
}

class NavigatorImpl(
    private val navControllerProvider: NavControllerProvider
) : Navigator {
    override fun navigateToDetails(id: String) {
        navControllerProvider.navController?.navigate(AppDestination.Details(id = id))
    }

    override fun navigateToAuth() {
        navControllerProvider.navController?.navigate(AppDestination.Auth)
    }

    override fun navigateToHome() {
        navControllerProvider.navController?.navigate(AppDestination.Home)
    }

    override fun navigateBack() {
        navControllerProvider.navController?.popBackStack()
    }
}