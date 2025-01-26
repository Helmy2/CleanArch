package com.example.cleanarch.presentation.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.cleanarch.presentation.auth.components.AuthAlternativeOptions
import com.example.cleanarch.presentation.auth.components.AuthButton
import com.example.cleanarch.presentation.auth.components.AuthHeader
import com.example.cleanarch.presentation.auth.components.AuthPasswordField
import com.example.cleanarch.presentation.auth.components.AuthTextField
import com.example.core.theme.CleanArchTheme


@Composable
fun AuthScreen(
    state: AuthState,
    onEvent: (AuthEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp)
            .imePadding()
            .navigationBarsPadding()
            .animateContentSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        AuthHeader(isRegistering = state.isRegistering)

        Spacer(Modifier.height(16.dp))

        AnimatedVisibility(
            visible = state.isRegistering,
        ) {
            Column {
                Spacer(Modifier.height(16.dp))
                AuthTextField(
                    value = state.name,
                    label = "Name",
                    error = state.nameError,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    onValueChange = { onEvent(AuthEvent.NameChanged(it)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        AuthTextField(
            value = state.email,
            label = "Email",
            error = state.emailError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
            ),
            onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        AuthPasswordField(
            value = state.password,
            error = state.passwordError,
            isVisible = state.isPasswordVisible,
            isRegistering = state.isRegistering,
            passwordStrength = state.passwordStrength,
            passwordRequirements = state.passwordRequirements,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) },
            onVisibilityToggle = { onEvent(AuthEvent.TogglePasswordVisibility) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(16.dp))

        AuthButton(
            isLoading = state.isLoading,
            isRegistering = state.isRegistering,
            onRegisterClick = {
                keyboardController?.hide()
                onEvent(AuthEvent.Register)
            },
            onLoginClick = {
                keyboardController?.hide()
                onEvent(AuthEvent.Login)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        AuthAlternativeOptions(
            onAnonymousLogin = { onEvent(AuthEvent.SignInAnonymously) },
            onAuthModeToggle = { onEvent(AuthEvent.ToggleAuthMode) },
            isRegistering = state.isRegistering,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@PreviewLightDark
@Composable
fun AuthScreenPreview_Login() {
    CleanArchTheme {
        Surface {
            AuthScreen(state = AuthState(), onEvent = {})
        }
    }
}

@PreviewLightDark
@Composable
fun AuthScreenPreview_Register() {
    CleanArchTheme {
        Surface {
            AuthScreen(state = AuthState(
                isRegistering = true,
                passwordStrength = PasswordStrength.MEDIUM,
                passwordRequirements = listOf(
                    Requirement("8+ characters", true), Requirement("1 uppercase letter", false)
                )
            ), onEvent = {})
        }
    }
}