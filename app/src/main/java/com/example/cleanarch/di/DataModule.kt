package com.example.cleanarch.di

import com.example.cleanarch.data.local.LocalAuthManager
import com.example.cleanarch.data.local.LocalAuthManagerImpl
import com.example.cleanarch.data.remote.RemoteAuthManager
import com.example.cleanarch.data.remote.RemoteAuthManagerImpl
import com.example.cleanarch.data.repository.AuthRepositoryImpl
import com.example.cleanarch.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val dataModule = module {
    includes(exceptionMapperModule)
    includes(firebaseModule)
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), Dispatchers.IO) }
    single<LocalAuthManager> { LocalAuthManagerImpl(get()) }
    single<RemoteAuthManager> { RemoteAuthManagerImpl(get(), get()) }
}