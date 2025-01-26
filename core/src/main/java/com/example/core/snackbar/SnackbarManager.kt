package com.example.core.snackbar

interface SnackbarManager {
    suspend fun showSnackbar(throwable: Throwable)
    suspend fun dismissSnackbar()
}