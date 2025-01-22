package com.example.cleanarch.presentation.home

import com.example.cleanarch.domain.entity.User

sealed class HomeEffect {
    data class ShowSnackbar(val message: String) : HomeEffect()
    data class NavigateToDetails(val id: Int) : HomeEffect()
}

data class HomeState(
    val user: User? = null,
    val isLoading: Boolean = true,
    val effect: HomeEffect? = null
)

sealed class HomeEvent {
    data class LoadUser(val id: Int) : HomeEvent()
    data object NavigateToDetails : HomeEvent()
    data object NavigationHandled : HomeEvent()
    data object DismissError : HomeEvent()
}