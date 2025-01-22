package com.example.cleanarch.presentation.main

import androidx.compose.runtime.Composable
import com.example.cleanarch.presentation.theme.CleanArchTheme
import org.koin.androidx.compose.KoinAndroidContext

@Composable
fun MainApp() {
    KoinAndroidContext {
        CleanArchTheme {
            MainNavigation()
        }
    }
}


