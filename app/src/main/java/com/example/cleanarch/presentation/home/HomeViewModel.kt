package com.example.cleanarch.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Resource
import com.example.cleanarch.presentation.common.navigation.Navigator
import com.example.cleanarch.presentation.common.snackbar.SnackbarManager
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
    private val getUserUseCase: com.example.domain.usecases.GetUserUseCase
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
                snackbarManager.showSnackbar(error)
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
                        snackbarManager.showSnackbar(result.exception)
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