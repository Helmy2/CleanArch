package com.example.feature.auth.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.core.theme.CleanArchTheme
import com.example.feature.auth.R
import com.example.feature.auth.presentation.components.AuthAlternativeOptions
import com.example.feature.auth.presentation.components.AuthButton
import com.example.feature.auth.presentation.components.AuthHeader
import com.example.feature.auth.presentation.components.AuthPasswordField
import com.example.feature.auth.presentation.components.AuthTextField


@Composable
fun AuthScreen(
    state: AuthState,
    onEvent: (AuthEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focus = LocalFocusManager.current

    BackHandler(enabled = state.authMode != AuthMode.Login) {
        onEvent(AuthEvent.AuthModeChanged(AuthMode.Login))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .imePadding()
            .navigationBarsPadding()
            .animateContentSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        AuthHeader(authMode = state.authMode, modifier = Modifier.height(300.dp))

        Spacer(Modifier.height(8.dp))

        AnimatedVisibility(
            visible = state.authMode == AuthMode.Register,
        ) {
            Column {
                Spacer(Modifier.height(8.dp))
                AuthTextField(
                    value = state.name,
                    label = stringResource(R.string.name),
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
            label = stringResource(R.string.email),
            error = state.emailError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = if (state.authMode == AuthMode.ForgotPassword) ImeAction.Done else ImeAction.Next
            ),
            onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(Modifier.height(8.dp))

        AnimatedVisibility(
            visible = state.authMode != AuthMode.ForgotPassword,
        ) {
            Column {
                Spacer(Modifier.height(8.dp))
                AuthPasswordField(
                    value = state.password,
                    error = state.passwordError,
                    isVisible = state.isPasswordVisible,
                    isRegistering = state.authMode == AuthMode.Register,
                    passwordStrength = state.passwordStrength,
                    passwordRequirements = state.passwordRequirements,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) },
                    onVisibilityToggle = { onEvent(AuthEvent.TogglePasswordVisibility) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        AnimatedVisibility(
            visible = state.authMode == AuthMode.Login,
            modifier = Modifier.align(Alignment.End)
        ) {
            Row(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .clickable { onEvent(AuthEvent.AuthModeChanged(AuthMode.ForgotPassword)) }
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = stringResource(R.string.forgot_password),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    stringResource(R.string.forgot_password),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        AuthButton(
            isLoading = state.isLoading,
            text = when (state.authMode) {
                AuthMode.Register -> stringResource(R.string.register)
                AuthMode.Login -> stringResource(R.string.login)
                AuthMode.ForgotPassword -> stringResource(R.string.reset_password)
            },
            onClick = {
                focus.clearFocus()
                onEvent(AuthEvent.AuthButtonClicked)
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(8.dp))

        AuthAlternativeOptions(
            onAnonymousLogin = { onEvent(AuthEvent.SignInAnonymously) },
            onAuthModeToggle = {
                focus.clearFocus()
                onEvent(AuthEvent.AuthModeChanged(it))
            },
            authMode = state.authMode,
            modifier = Modifier.fillMaxWidth(),
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
            AuthScreen(
                state = AuthState(
                    authMode = AuthMode.Register,
                    passwordStrength = PasswordStrength.MEDIUM,
                    passwordRequirements = listOf(
                        Requirement(R.string.password_requirement_special, true),
                        Requirement(R.string.password_requirement_uppercase, false)
                    )
                ),
                onEvent = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
fun AuthScreenPreview_ForgotPassword() {
    CleanArchTheme {
        Surface {
            AuthScreen(
                state = AuthState(
                    authMode = AuthMode.ForgotPassword,
                ),
                onEvent = {},
            )
        }
    }
}