package com.example.cleanarch.di

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController

class NavControllerProvider {
    var navController: NavController? = null
}

class SnackbarHostStateProvider {
    var snackbarHostState: SnackbarHostState? = null
}