package com.example.feature.auth.presentation

import android.os.Handler
import android.os.Looper
import androidx.annotation.StringRes
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.feature.auth.R

class FakeAuthViewModel {
    private val _state = mutableStateOf(AuthState())
    val state: State<AuthState> = _state

    private val _events = mutableListOf<AuthEvent>()
    val events: List<AuthEvent> get() = _events.toList()

    fun handleEvent(event: AuthEvent) {
        _events.add(event)
        when (event) {
            is AuthEvent.EmailChanged -> updateState { copy(email = event.email) }
            is AuthEvent.PasswordChanged -> updateState { copy(password = event.password) }
            is AuthEvent.NameChanged -> updateState { copy(name = event.name) }
            is AuthEvent.AuthModeChanged -> updateState { copy(authMode = event.authMode) }
            AuthEvent.TogglePasswordVisibility -> updateState {
                copy(isPasswordVisible = !isPasswordVisible)
            }

            AuthEvent.AuthButtonClicked -> simulateAuthProcess()
            AuthEvent.SignInAnonymously -> simulateAnonymousSignIn()
        }
    }

    fun setErrorState(
        @StringRes emailError: Int? = null,
        @StringRes passwordError: Int? = null
    ) {
        updateState { copy(emailError = emailError, passwordError = passwordError) }
    }

    private fun simulateAuthProcess() {
        updateState { copy(isLoading = true) }
        // Simulate network delay
        Handler(Looper.getMainLooper()).postDelayed({
            updateState {
                copy(
                    isLoading = false,
                    emailError = if (email.isEmpty()) R.string.error_invalid_email else null,
                    passwordError = if (password.isEmpty()) R.string.error_password_empty else null
                )
            }
        }, 1000)
    }

    private fun simulateAnonymousSignIn() {
        updateState { copy(isLoading = true) }
        Handler(Looper.getMainLooper()).postDelayed({
            updateState { copy(isLoading = false) }
        }, 500)
    }

    private inline fun updateState(transform: AuthState.() -> AuthState) {
        _state.value = _state.value.transform()
    }
}