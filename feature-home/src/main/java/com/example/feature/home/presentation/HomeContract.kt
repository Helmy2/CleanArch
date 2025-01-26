package com.example.feature.home.presentation

import com.example.domain.entity.User


data class HomeState(
    val user: User? = null,
    val isLoading: Boolean = true,
)

sealed class HomeEvent {
    data class LoadUser(val id: Int) : HomeEvent()
    data object NavigateToDetails : HomeEvent()
}