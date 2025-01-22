package com.example.cleanarch.presentation.common.snackbar

interface SnackbarManager {
    suspend fun showSnackbar(throwable: Throwable)
    suspend fun dismissSnackbar()
}