package com.example.feature.auth.presentation

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.navigation.Navigator
import com.example.core.snackbar.SnackbarManager
import com.example.feature.auth.R
import com.example.feature.auth.domain.usecase.LoginUseCase
import com.example.feature.auth.domain.usecase.RegisterUseCase
import com.example.feature.auth.domain.usecase.RestPasswordUseCase
import com.example.feature.auth.domain.usecase.SignInAnonymouslyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase,
    private val restPasswordUseCase: RestPasswordUseCase,
    private val snackbarManager: SnackbarManager,
    private val navigator: Navigator
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    fun handleEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.EmailChanged -> {
                updateEmail(event.email)
                clearEmailError()
            }

            is AuthEvent.PasswordChanged -> {
                updatePassword(event.password)
                updatePasswordStrength(event.password)
                clearPasswordError()
            }

            is AuthEvent.NameChanged -> {
                updateName(event.name)
                clearNameError()
            }

            is AuthEvent.AuthModeChanged -> toggleAuthMode(event.authMode)
            AuthEvent.TogglePasswordVisibility -> togglePasswordVisibility()
            AuthEvent.SignInAnonymously -> signInAnonymously()
            AuthEvent.AuthButtonClicked -> {
                when (state.value.authMode) {
                    AuthMode.ForgotPassword -> forgetPassword()
                    AuthMode.Login -> login()
                    AuthMode.Register -> register()
                }

            }
        }
    }

    private fun togglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }


    private fun login() {
        if (!validateLoginInputs()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = loginUseCase(state.value.email, state.value.password)
            handleAuthResult(result) {
                navigator.navigateToHome()
            }
        }
    }

    private fun register() {
        if (!validateRegisterInputs()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = registerUseCase(
                email = state.value.email, password = state.value.password, name = state.value.name
            )
            handleAuthResult(result) {
                navigator.navigateToHome()
            }
        }
    }

    private fun forgetPassword() {
        if (!isValidEmail(_state.value.email)) {
            _state.update { it.copy(emailError = R.string.error_invalid_email) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = restPasswordUseCase(state.value.email)
            handleAuthResult(result) {
                _state.update { it.copy(authMode = AuthMode.Login) }
                snackbarManager.showSnackbar("Check your email to reset your password")
            }
        }
    }

    private fun signInAnonymously() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = signInAnonymouslyUseCase()
            handleAuthResult(result) {
                navigator.navigateToHome()
            }
        }
    }

    private fun toggleAuthMode(authMode: AuthMode) {
        _state.update {
            it.copy(
                authMode = authMode, nameError = null, passwordError = null, emailError = null
            )
        }
    }

    private suspend fun handleAuthResult(result: Result<Unit>, onSuccess: suspend () -> Unit) {
        _state.update { it.copy(isLoading = false) }
        result.fold(
            onSuccess = { onSuccess() },
            onFailure = {
                snackbarManager.showSnackbar(it.localizedMessage.orEmpty())
            },
        )
    }

    // Helper functions to update state
    private fun updateEmail(email: String) {
        _state.update { it.copy(email = email) }
    }

    private fun updatePassword(password: String) {
        _state.update { it.copy(password = password) }
    }

    private fun updateName(name: String) {
        _state.update { it.copy(name = name) }
    }

    // Clear errors when user starts typing
    private fun clearEmailError() {
        _state.update { it.copy(emailError = null) }
    }

    private fun clearPasswordError() {
        _state.update { it.copy(passwordError = null) }
    }

    private fun clearNameError() {
        _state.update { it.copy(nameError = null) }
    }

    private fun updatePasswordStrength(password: String) {
        val strength = calculatePasswordStrength(password)
        val requirements = calculatePasswordRequirements(password)
        _state.update {
            it.copy(
                passwordStrength = strength, passwordRequirements = requirements
            )
        }
    }

    // Enhanced validation
    private fun validateLoginInputs(): Boolean {
        val emailValid = isValidEmail(state.value.email)
        val passwordValid = state.value.password.length >= 8

        _state.update {
            it.copy(
                emailError = if (emailValid) null else R.string.error_invalid_email,
                passwordError = if (passwordValid) null else R.string.error_invalid_password,
            )
        }

        return emailValid && passwordValid
    }

    private fun validateRegisterInputs(): Boolean {
        val emailValid = isValidEmail(state.value.email)
        val passwordValid = calculatePasswordStrength(state.value.password) != PasswordStrength.WEAK
        val nameValid = state.value.name.isNotBlank()

        _state.update {
            it.copy(
                emailError = if (emailValid) null else R.string.error_invalid_email, passwordError = when {
                    passwordValid -> null
                    state.value.password.isBlank() -> R.string.error_password_empty
                    else -> R.string.error_password_weak
                }, nameError = if (nameValid) null else R.string.error_name_required
            )
        }
        return emailValid && passwordValid && nameValid
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun calculatePasswordStrength(password: String): PasswordStrength {
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        val hasUppercase = password.any { it.isUpperCase() }
        val length = password.length

        return when {
            length >= 8 && hasSpecialChar && hasUppercase -> PasswordStrength.STRONG
            length >= 8 && (hasSpecialChar || hasUppercase) -> PasswordStrength.MEDIUM
            else -> PasswordStrength.WEAK
        }
    }

    private fun calculatePasswordRequirements(password: String): List<Requirement> {
        return listOf(
            Requirement(R.string.password_requirement_length, password.length >= 8),
            Requirement(R.string.password_requirement_special, password.any { !it.isLetterOrDigit() }),
            Requirement(R.string.password_requirement_uppercase, password.any { it.isUpperCase() })
        )
    }
}