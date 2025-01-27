package com.example.core.snackbar

import androidx.compose.material3.SnackbarHostState

class SnackbarHostStateProvider {
    var snackbarHostState: SnackbarHostState? = null
}

class SnackbarManagerImpl(
    private val snackbarHostStateProvider: SnackbarHostStateProvider
) : SnackbarManager {
    override suspend fun showSnackbar(value: String) {
        dismissSnackbar()
        snackbarHostStateProvider.snackbarHostState?.showSnackbar(
            value.ifEmpty { "Unknown error" }
        )
    }

    override suspend fun dismissSnackbar() {
        snackbarHostStateProvider.snackbarHostState?.currentSnackbarData?.dismiss()
    }
}