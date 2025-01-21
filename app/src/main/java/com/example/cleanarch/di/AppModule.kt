package com.example.cleanarch.di

import com.example.cleanarch.domain.repository.UserRepository
import com.example.cleanarch.domain.usecas.GetUserUseCase
import com.example.cleanarch.data.repository.UserRepositoryImpl
import com.example.cleanarch.presentation.user.UserViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val userModule = module {
    single<UserRepository> { UserRepositoryImpl() }
    factory { GetUserUseCase(get()) }
    viewModel { UserViewModel(get()) }
}

val appModule = module {
    includes(userModule)
}