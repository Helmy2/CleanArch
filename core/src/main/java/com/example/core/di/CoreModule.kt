package com.example.core.di


import com.example.core.navigation.NavControllerProvider
import com.example.core.navigation.Navigator
import com.example.core.navigation.NavigatorImpl
import com.example.core.snackbar.SnackbarHostStateProvider
import com.example.core.snackbar.SnackbarManager
import com.example.core.snackbar.SnackbarManagerImpl
import org.koin.dsl.module

val coreModule = module {
    single<Navigator> { NavigatorImpl(get()) }
    single { NavControllerProvider() }

    single<SnackbarManager> { SnackbarManagerImpl(get()) }
    single { SnackbarHostStateProvider() }
}