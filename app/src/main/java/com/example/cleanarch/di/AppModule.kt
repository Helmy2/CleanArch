package com.example.cleanarch.di

import com.example.core.di.coreModule
import org.koin.dsl.module

val appModule = module {
    includes(coreModule)
    includes(dataStoreModule)
    includes(presentationModule)
    includes(dataModule)
}