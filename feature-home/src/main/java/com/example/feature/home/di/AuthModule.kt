package com.example.feature.home.di

import com.example.feature.home.presentation.HomeViewModel
import com.example.feature.home.usecase.GetUserUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val homeModule = module {
    factory { GetUserUseCase(get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
}