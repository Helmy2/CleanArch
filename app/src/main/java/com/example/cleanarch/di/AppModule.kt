package com.example.cleanarch.di

import com.example.core.di.coreModule
import com.example.data.di.dataModule
import com.example.feature.auth.di.authModule
import com.example.feature.home.di.homeModule
import org.koin.dsl.module

val appModule = module {
    includes(coreModule)
    includes(dataModule)
    includes(authModule)
    includes(homeModule)
}