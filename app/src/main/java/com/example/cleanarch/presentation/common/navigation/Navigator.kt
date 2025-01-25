package com.example.cleanarch.presentation.common.navigation

interface Navigator {
    fun navigateToDetails(id: String)
    fun navigateToAuth()
    fun navigateToHome()
    fun navigateBack()
}