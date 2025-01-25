package com.example.cleanarch.di

import com.example.cleanarch.domain.usecas.GetUserUseCase
import com.example.cleanarch.domain.usecas.LoginUseCase
import com.example.cleanarch.domain.usecas.RegisterUseCase
import com.example.cleanarch.domain.usecas.SignInAnonymouslyUseCase
import com.example.cleanarch.presentation.auth.AuthViewModel
import com.example.cleanarch.presentation.common.navigation.Navigator
import com.example.cleanarch.presentation.common.navigation.NavigatorImpl
import com.example.cleanarch.presentation.common.snackbar.SnackbarManager
import com.example.cleanarch.presentation.common.snackbar.SnackbarManagerImpl
import com.example.cleanarch.presentation.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val navigatorModule = module {
    single<Navigator> { NavigatorImpl(get()) }
    single { NavControllerProvider() }
}

val snackbarModule = module {
    single<SnackbarManager> { SnackbarManagerImpl(get()) }
    single { SnackbarHostStateProvider() }
}

val userModule = module {
    factory { GetUserUseCase(get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
}

val authModule = module {
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { SignInAnonymouslyUseCase(get()) }
    viewModel { AuthViewModel(get(), get(), get(), get(), get()) }
}


val presentationModule = module {
    includes(navigatorModule)
    includes(snackbarModule)
    includes(userModule)
    includes(authModule)
}