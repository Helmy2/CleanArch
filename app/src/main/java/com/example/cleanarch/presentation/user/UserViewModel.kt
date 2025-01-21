package com.example.cleanarch.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarch.domain.entity.User
import com.example.cleanarch.domain.usecas.GetUserUseCase
import kotlinx.coroutines.launch
import com.example.cleanarch.domain.entity.DomainResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _userState = MutableStateFlow<DomainResult<User>>(DomainResult.Loading)
    val userState: StateFlow<DomainResult<User>> = _userState

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            getUserUseCase(userId)
                .collect { result ->
                    _userState.value = result
                }
        }
    }
}