package com.example.core.snackbar

import androidx.compose.material3.SnackbarHostState


class SnackbarManagerImpl : SnackbarManager {
    override var snackbarHostState: SnackbarHostState? = null

    override suspend fun showSnackbar(value: String) {
        dismissSnackbar()
        snackbarHostState?.showSnackbar(
            value.ifEmpty { "Unknown error" }
        )
    }

    override suspend fun dismissSnackbar() {
        snackbarHostState?.currentSnackbarData?.dismiss()
    }
}