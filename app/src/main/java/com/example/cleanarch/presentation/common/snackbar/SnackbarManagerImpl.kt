package com.example.cleanarch.presentation.common.snackbar

import com.example.cleanarch.di.SnackbarHostStateProvider

class SnackbarManagerImpl(
    val snackbarHostStateProvider: SnackbarHostStateProvider
) : SnackbarManager {
    override suspend fun showSnackbar(message: String) {
        dismissSnackbar()
        snackbarHostStateProvider.snackbarHostState?.showSnackbar(message)
    }

    override suspend fun dismissSnackbar() {
        snackbarHostStateProvider.snackbarHostState?.currentSnackbarData?.dismiss()
    }
}