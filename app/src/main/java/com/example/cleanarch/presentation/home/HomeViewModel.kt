package com.example.cleanarch.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarch.domain.entity.DomainResult
import com.example.cleanarch.domain.usecas.GetUserUseCase
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
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.onStart { loadUserData(0) }.stateIn(
        viewModelScope,
        //Keep state alive for 5s after UI stops observing
        SharingStarted.WhileSubscribed(5000), HomeState()
    )

    private fun loadUserData(id: Int) {
        viewModelScope.launch {
            getUserUseCase(id).catch { error ->
                _state.update { it.copy(isLoading = false) }
                snackbarManager.showSnackbar(error.localizedMessage ?: "Unknown error")
            }.collect { result ->
                when (result) {
                    DomainResult.Loading -> _state.update {
                        it.copy(isLoading = true)
                    }

                    is DomainResult.Success -> _state.update {
                        it.copy(isLoading = false, user = result.data)
                    }

                    is DomainResult.Failure -> {
                        _state.update { it.copy(isLoading = false) }
                        snackbarManager.showSnackbar(
                            result.exception.localizedMessage ?: "Unknown error"
                        )
                    }
                }
            }
        }
    }

    fun handleEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.NavigateToDetails -> {
                navigator.navigateToDetails(_state.value.user?.id ?: -1)
            }

            is HomeEvent.LoadUser -> loadUserData(event.id)
        }
    }
}