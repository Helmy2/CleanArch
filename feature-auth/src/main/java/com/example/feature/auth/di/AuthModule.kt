package com.example.feature.auth.di

import com.example.feature.auth.presentation.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val authModule = module {
    viewModel {
        AuthViewModel(get(), get(), get(), get(), get(), get ())
    }
}