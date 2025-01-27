package com.example.feature.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.feature.auth.presentation.AuthScreen
import com.example.feature.auth.presentation.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthRoute(
    viewModel: AuthViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AuthScreen(
        state = state,
        onEvent = viewModel::handleEvent
    )
}
