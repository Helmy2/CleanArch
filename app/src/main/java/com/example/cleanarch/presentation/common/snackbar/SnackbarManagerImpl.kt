package com.example.cleanarch.presentation.common.snackbar

import com.example.cleanarch.di.SnackbarHostStateProvider

class SnackbarManagerImpl(
    val snackbarHostStateProvider: SnackbarHostStateProvider
) : SnackbarManager {
    override suspend fun showSnackbar(throwable: Throwable) {
        dismissSnackbar()
        snackbarHostStateProvider.snackbarHostState?.showSnackbar(
            throwable.localizedMessage ?: "Unknown error"
        )
    }

    override suspend fun dismissSnackbar() {
        snackbarHostStateProvider.snackbarHostState?.currentSnackbarData?.dismiss()
    }
}