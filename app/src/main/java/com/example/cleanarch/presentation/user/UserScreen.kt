package com.example.cleanarch.presentation.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.cleanarch.domain.entity.DomainResult
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserScreen(viewModel: UserViewModel = koinViewModel()) {
    val userState by viewModel.userState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUser(1)
    }
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (userState) {
                is DomainResult.Loading -> {
                    Text(text = "Loading...")
                }

                is DomainResult.Success -> {
                    val user = (userState as DomainResult.Success).data
                    Text(text = "User: ${user.name}")
                }

                is DomainResult.Error -> {
                    val error = (userState as DomainResult.Error).exception
                    Text(text = "Error: ${error.message}")
                }
            }
        }
    }
}