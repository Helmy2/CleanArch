package com.example.domain.di

import com.example.domain.usecase.GetUserUseCase
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.RegisterUseCase
import com.example.domain.usecase.RestPasswordUseCase
import com.example.domain.usecase.SignInAnonymouslyUseCase
import org.koin.dsl.module


val domainModule = module {
    factory { GetUserUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { SignInAnonymouslyUseCase(get()) }
    factory { RestPasswordUseCase(get()) }
}