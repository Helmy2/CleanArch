package com.example.cleanarch

import android.app.Application
import com.example.cleanarch.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CleanArchApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CleanArchApp)
            modules(appModule)
        }
    }
}