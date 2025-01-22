package com.example.cleanarch.presentation.common.snackbar

interface SnackbarManager {
    suspend fun showSnackbar(message: String)
    suspend fun dismissSnackbar()
}