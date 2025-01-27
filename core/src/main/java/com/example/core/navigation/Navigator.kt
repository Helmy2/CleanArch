package com.example.core.navigation

import androidx.navigation.NavHostController

interface Navigator {
    var navController: NavHostController?
    fun navigateToDetails(id: String)
    fun navigateToAuth()
    fun navigateToHome()
    fun navigateBack()
    fun navigateToTopLevelRute(route: Destination)
}