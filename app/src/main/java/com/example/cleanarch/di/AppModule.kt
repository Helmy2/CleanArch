package com.example.cleanarch.di

import com.example.core.di.coreModule
import com.example.data.di.dataModule
import org.koin.dsl.module

val appModule = module {
    includes(coreModule)
    includes(dataStoreModule)
    includes(presentationModule)
    includes(dataModule)
}