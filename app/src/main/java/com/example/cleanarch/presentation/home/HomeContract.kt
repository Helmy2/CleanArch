package com.example.cleanarch.presentation.home

import com.example.domain.entity.User


data class HomeState(
    val user: com.example.domain.entity.User? = null,
    val isLoading: Boolean = true,
)

sealed class HomeEvent {
    data class LoadUser(val id: Int) : HomeEvent()
    data object NavigateToDetails : HomeEvent()
}