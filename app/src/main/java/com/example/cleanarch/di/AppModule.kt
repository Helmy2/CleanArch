package com.example.cleanarch.di

import com.example.cleanarch.data.local.LocalUserManager
import com.example.cleanarch.data.local.LocalUserManagerImpl
import com.example.cleanarch.data.remote.RemoteUserManager
import com.example.cleanarch.data.remote.RemoteUserManagerImpl
import com.example.cleanarch.data.repository.UserRepositoryImpl
import com.example.cleanarch.domain.repository.UserRepository
import com.example.cleanarch.domain.usecas.GetUserUseCase
import com.example.cleanarch.presentation.common.navigation.Navigator
import com.example.cleanarch.presentation.common.navigation.NavigatorImpl
import com.example.cleanarch.presentation.common.snackbar.SnackbarManager
import com.example.cleanarch.presentation.common.snackbar.SnackbarManagerImpl
import com.example.cleanarch.presentation.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val dataModule = module {
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<LocalUserManager> { LocalUserManagerImpl() }
    single<RemoteUserManager> { RemoteUserManagerImpl() }
}

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
    includes(userModule)
    includes(dataModule)
    includes(navigatorModule)
    includes(snackbarModule)
}