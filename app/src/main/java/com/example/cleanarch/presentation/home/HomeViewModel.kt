package com.example.cleanarch.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarch.domain.entity.DomainResult
import com.example.cleanarch.domain.usecas.GetUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state
        .onStart { loadUserData(1) }
        .stateIn(
            viewModelScope,
            //Keep state alive for 5s after UI stops observing
            SharingStarted.WhileSubscribed(5000),
            HomeState()
        )

    private fun loadUserData(id: Int) {
        viewModelScope.launch {
            getUserUseCase(id)
                .catch { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            effect = HomeEffect.ShowSnackbar(error.message ?: "Unknown error")
                        )
                    }
                }
                .collect { result ->
                    when (result) {
                        DomainResult.Loading -> _state.update {
                            it.copy(isLoading = true, effect = null)
                        }

                        is DomainResult.Success -> _state.update {
                            it.copy(
                                isLoading = false,
                                user = result.data,
                                effect = null
                            )
                        }

                        is DomainResult.Failure -> _state.update {
                            it.copy(
                                isLoading = false,
                                effect = HomeEffect.ShowSnackbar(
                                    result.exception.localizedMessage ?: "Unknown error"
                                )
                            )
                        }
                    }
                }
        }
    }

    fun handleEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.NavigateToDetails -> {
                _state.update { it.copy(effect = HomeEffect.NavigateToDetails(it.user?.id ?: -1)) }
            }

            is HomeEvent.NavigationHandled -> {
                _state.update { it.copy(effect = null) }
            }

            is HomeEvent.DismissError -> {
                _state.update { it.copy(effect = null) }
            }

            is HomeEvent.LoadUser -> loadUserData(event.id)
        }
    }
}