package com.example.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.navigation.Navigator
import com.example.core.snackbar.SnackbarManager
import com.example.domain.entity.Resource
import com.example.feature.home.usecase.GetUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val navigator: Navigator,
    private val snackbarManager: SnackbarManager,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.onStart { loadUserData() }.stateIn(
        viewModelScope,
        //Keep state alive for 5s after UI stops observing
        SharingStarted.WhileSubscribed(5000), HomeState()
    )

    private fun loadUserData() {
        viewModelScope.launch {
            getUserUseCase().catch { error ->
                _state.update { it.copy(isLoading = false) }
                snackbarManager.showSnackbar(error.localizedMessage.orEmpty())
            }.collect { result ->
                when (result) {
                    Resource.Loading -> _state.update {
                        it.copy(isLoading = true)
                    }

                    is Resource.Success -> _state.update {
                        it.copy(isLoading = false, user = result.data)
                    }

                    is Resource.Failure -> {
                        _state.update { it.copy(isLoading = false) }
                        snackbarManager.showSnackbar(result.exception.localizedMessage.orEmpty())
                    }
                }
            }
        }
    }

    fun handleEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.NavigateToDetails -> {
                navigator.navigateToDetails(_state.value.user?.id ?: "")
            }

            is HomeEvent.LoadUser -> loadUserData()
        }
    }
}