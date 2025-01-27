package com.example.feature.auth.di

import com.example.feature.auth.domain.usecase.LoginUseCase
import com.example.feature.auth.domain.usecase.RegisterUseCase
import com.example.feature.auth.domain.usecase.RestPasswordUseCase
import com.example.feature.auth.domain.usecase.SignInAnonymouslyUseCase
import com.example.feature.auth.presentation.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val authModule = module {
    viewModel {
        AuthViewModel(get(), get(), get(), get(), get(), get ())
    }
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { SignInAnonymouslyUseCase(get()) }
    factory { RestPasswordUseCase(get()) }
}