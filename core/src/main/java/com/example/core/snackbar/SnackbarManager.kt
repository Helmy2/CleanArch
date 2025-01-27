package com.example.core.snackbar

import androidx.compose.material3.SnackbarHostState

interface SnackbarManager {
    var snackbarHostState: SnackbarHostState?
    suspend fun showSnackbar(value: String)
    suspend fun dismissSnackbar()
}