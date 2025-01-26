package com.example.cleanarch.presentation.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarch.domain.usecas.LoginUseCase
import com.example.cleanarch.domain.usecas.RegisterUseCase
import com.example.cleanarch.domain.usecas.SignInAnonymouslyUseCase
import com.example.cleanarch.presentation.common.navigation.Navigator
import com.example.cleanarch.presentation.common.snackbar.SnackbarManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase,
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

            AuthEvent.TogglePasswordVisibility -> togglePasswordVisibility()
            AuthEvent.ToggleAuthMode -> toggleAuthMode()
            AuthEvent.Login -> login()
            AuthEvent.Register -> register()
            AuthEvent.SignInAnonymously -> signInAnonymously()
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
            handleAuthResult(result)
        }
    }

    private fun register() {
        if (!validateRegisterInputs()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = registerUseCase(
                email = state.value.email, password = state.value.password, name = state.value.name
            )
            handleAuthResult(result)
        }
    }

    private fun signInAnonymously() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = signInAnonymouslyUseCase()
            handleAuthResult(result)
        }
    }

    private fun toggleAuthMode() {
        _state.update {
            it.copy(
                isRegistering = !it.isRegistering,
                name = if (it.isRegistering) "" else it.name,
                nameError = null
            )
        }
    }

    private fun handleAuthResult(result: Result<Unit>) {
        _state.update { it.copy(isLoading = false) }
        result.fold(onSuccess = {
            navigator.navigateToHome()
        }, onFailure = {
            viewModelScope.launch {
                snackbarManager.showSnackbar(it)
            }
        })
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
                passwordStrength = strength,
                passwordRequirements = requirements
            )
        }
    }

    // Enhanced validation
    private fun validateLoginInputs(): Boolean {
        val emailValid = isValidEmail(state.value.email)
        val passwordValid = state.value.password.length >= 8

        _state.update {
            it.copy(
                emailError = if (emailValid) null else "Invalid email",
                passwordError = if (passwordValid) null else "Invalid Password",
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
                emailError = if (emailValid) null else "Invalid email",
                passwordError = when {
                    passwordValid -> null
                    state.value.password.isNotBlank() -> "The password i empty"
                    else -> "The password should be medium or strong"
                },
                nameError = if (nameValid) null else "Name is required"
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
            Requirement("At least 8 characters", password.length >= 8),
            Requirement("Contains a special character", password.any { !it.isLetterOrDigit() }),
            Requirement("Contains an uppercase letter", password.any { it.isUpperCase() })
        )
    }
}