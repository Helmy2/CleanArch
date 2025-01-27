package com.example.feature.home.di

import com.example.feature.home.presentation.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val homeModule = module {
    viewModel { HomeViewModel(get(), get(), get()) }
}