package com.example.core.snackbar

interface SnackbarManager {
    suspend fun showSnackbar(value: String)
    suspend fun dismissSnackbar()
}