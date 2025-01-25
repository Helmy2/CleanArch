package com.example.cleanarch.di

import org.koin.dsl.module

val appModule = module {
    includes(dataStoreModule)
    includes(presentationModule)
    includes(dataModule)
}