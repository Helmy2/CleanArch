package com.example.cleanarch.di

import com.example.cleanarch.domain.usecas.GetUserUseCase
import com.example.cleanarch.presentation.common.navigation.Navigator
import com.example.cleanarch.presentation.common.navigation.NavigatorImpl
import com.example.cleanarch.presentation.common.snackbar.SnackbarManager
import com.example.cleanarch.presentation.common.snackbar.SnackbarManagerImpl
import com.example.cleanarch.presentation.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val userModule = module {
    factory { GetUserUseCase(get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
}

val navigatorModule = module {
    single<Navigator> { NavigatorImpl(get()) }
    single { NavControllerProvider() }
}

val snackbarModule = module {
    single<SnackbarManager> { SnackbarManagerImpl(get()) }
    single { SnackbarHostStateProvider() }
}

val appModule = module {
    includes(navigatorModule)
    includes(snackbarModule)
    includes(exceptionMapperModule)
    includes(dataStoreModule)
    includes(firebaseModule)
    includes(userModule)
    includes(dataModule)
}