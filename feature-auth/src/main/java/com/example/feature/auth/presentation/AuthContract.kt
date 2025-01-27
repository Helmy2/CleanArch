package com.example.feature.auth.presentation

import androidx.annotation.StringRes

data class AuthState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val isLoading: Boolean = false,
    val authMode: AuthMode = AuthMode.Login,
    @StringRes val emailError: Int? = null,
    @StringRes val passwordError: Int? = null,
    @StringRes val nameError: Int? = null,
    val isPasswordVisible: Boolean = false,
    val passwordStrength: PasswordStrength = PasswordStrength.WEAK,
    val passwordRequirements: List<Requirement> = emptyList()
)

enum class PasswordStrength(val strengthValue: Float) {
    WEAK(0.33f),
    MEDIUM(0.66f),
    STRONG(1f)
}

data class Requirement(
    @StringRes val text: Int,
    val isMet: Boolean
)

sealed class AuthMode {
    data object Login : AuthMode()
    data object Register : AuthMode()
    data object ForgotPassword : AuthMode()
}

sealed class AuthEvent {
    data class EmailChanged(val email: String) : AuthEvent()
    data class PasswordChanged(val password: String) : AuthEvent()
    data class NameChanged(val name: String) : AuthEvent()
    data class AuthModeChanged(val authMode: AuthMode) : AuthEvent()
    data object TogglePasswordVisibility : AuthEvent()
    data object AuthButtonClicked : AuthEvent()
    data object SignInAnonymously : AuthEvent()
}