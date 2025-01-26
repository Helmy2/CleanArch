package com.example.data.di

import com.example.data.exception.AuthExceptionMapper
import com.example.data.local.LocalAuthManager
import com.example.data.local.LocalAuthManagerImpl
import com.example.data.remote.RemoteAuthManager
import com.example.data.remote.RemoteAuthManagerImpl
import com.example.data.repository.AuthRepositoryImpl
import com.example.domain.exceptions.ExceptionMapper
import com.example.domain.repository.AuthRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val dataModule = module {
    single<ExceptionMapper> { AuthExceptionMapper() }
    single<AuthRepository> {
        AuthRepositoryImpl(
            get(),
            get(),
            Dispatchers.IO
        )
    }
    single { Firebase.auth }

    single<LocalAuthManager> {
        LocalAuthManagerImpl(
            get()
        )
    }
    single<RemoteAuthManager> {
        RemoteAuthManagerImpl(
            get(),
            get()
        )
    }
}