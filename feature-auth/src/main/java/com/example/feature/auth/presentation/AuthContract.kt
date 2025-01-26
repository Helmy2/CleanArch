package com.example.feature.auth.presentation

data class AuthState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val isLoading: Boolean = false,
    val isRegistering: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val nameError: String? = null,
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
    val text: String,
    val isMet: Boolean
)

sealed class AuthEvent {
    data class EmailChanged(val email: String) : AuthEvent()
    data class PasswordChanged(val password: String) : AuthEvent()
    data class NameChanged(val name: String) : AuthEvent()
    data object TogglePasswordVisibility : AuthEvent()
    data object ToggleAuthMode : AuthEvent()
    data object Login : AuthEvent()
    data object Register : AuthEvent()
    data object SignInAnonymously : AuthEvent()
}