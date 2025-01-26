package com.example.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

const val USER_PREFERENCES = "user_preferences"

val dataModule = module {
    single<ExceptionMapper> { AuthExceptionMapper() }
    single<AuthRepository> {
        AuthRepositoryImpl(
            get(), get(), Dispatchers.IO
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
            get(), get()
        )
    }
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(produceNewData = { emptyPreferences() }),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = {
                androidContext().applicationContext.preferencesDataStoreFile(
                    USER_PREFERENCES
                )
            },
        )
    }
}

