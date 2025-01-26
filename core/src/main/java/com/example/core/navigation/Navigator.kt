package com.example.core.navigation

interface Navigator {
    fun navigateToDetails(id: String)
    fun navigateToAuth()
    fun navigateToHome()
    fun navigateBack()
}