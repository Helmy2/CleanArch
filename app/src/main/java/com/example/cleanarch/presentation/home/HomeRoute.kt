package com.example.cleanarch.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cleanarch.domain.entity.User
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToDetails: (id: Int) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.effect) {
        state.effect?.let { effect ->
            when (effect) {
                is HomeEffect.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(effect.message)
                    if (result == SnackbarResult.Dismissed) {
                        viewModel.handleEvent(HomeEvent.DismissError)
                    }
                }

                is HomeEffect.NavigateToDetails -> {
                    onNavigateToDetails(effect.id)
                    viewModel.handleEvent(HomeEvent.NavigationHandled)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        when {
            state.isLoading -> FullScreenLoader()
            state.user != null -> UserContent(
                user = state.user!!,
                onDetailsClick = { viewModel.handleEvent(HomeEvent.NavigateToDetails) },
                paddingValues = paddingValues
            )

            else -> ErrorState(onRetry = { viewModel.handleEvent(HomeEvent.LoadUser(1)) })
        }
    }
}

@Composable
private fun UserContent(
    user: User,
    onDetailsClick: () -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome, ${user.name}!", style = typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onDetailsClick) {
            Text("View Details")
        }
    }
}

@Composable
private fun FullScreenLoader() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Error loading data", color = colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}