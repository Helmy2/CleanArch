package com.example.cleanarch.presentation.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.cleanarch.presentation.theme.CleanArchTheme


@Composable
fun AuthScreen(
    state: AuthState,
    onEvent: (AuthEvent) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
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
                Spacer(Modifier.height(8.dp))
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

        Spacer(Modifier.height(8.dp))

        AuthTextField(
            value = state.email,
            label = "Email",
            error = state.emailError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

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
            onClick = {
                keyboardController?.hide()
                if (state.isRegistering) onEvent(AuthEvent.Register)
                else onEvent(AuthEvent.Login)
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
private fun AuthScreenPrev1() {
    CleanArchTheme {
        AuthScreen(state = AuthState(), onEvent = {})
    }
}

@PreviewLightDark()
@Composable
private fun AuthScreenPrev2() {
    CleanArchTheme {
        AuthScreen(state = AuthState(
            isRegistering = true
        ), onEvent = {})
    }
}

@Composable
fun AuthHeader(isRegistering: Boolean) {
    AnimatedContent(
        targetState = isRegistering,
    ) { registering ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    )
                    .padding(12.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = if (registering) "Create Account" else "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.animateContentSize()
            )

            Spacer(Modifier.height(8.dp))

            Crossfade(targetState = registering) {
                Text(
                    text = if (it) "Join our community today!"
                    else "Sign in to continue your journey",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun AuthTextField(
    value: String,
    label: String,
    error: String?,
    keyboardOptions: KeyboardOptions,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(label) },
            keyboardOptions = keyboardOptions,
            singleLine = true,
            isError = error != null,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            supportingText = {
                AnimatedVisibility(visible = error != null) {
                    Text(
                        text = error.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        )
    }
}

@Composable
fun AuthPasswordField(
    value: String,
    error: String?,
    isVisible: Boolean,
    isRegistering: Boolean,
    passwordStrength: PasswordStrength,
    passwordRequirements: List<Requirement>,
    keyboardOptions: KeyboardOptions,
    onValueChange: (String) -> Unit,
    onVisibilityToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Password") },
        keyboardOptions = keyboardOptions,
        visualTransformation = if (isVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        singleLine = true,
        isError = error != null,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        trailingIcon = {
            PasswordVisibilityToggle(
                isVisible = isVisible,
                onToggle = onVisibilityToggle
            )
        },
        supportingText = {
            AnimatedContent(error != null, modifier = Modifier.animateContentSize()) {
                if (it) {
                    Text(
                        text = error.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                } else if (isRegistering && value.isNotBlank()) {
                    PasswordStrengthIndicator(
                        strength = passwordStrength,
                        requirements = passwordRequirements,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
    )
}

@Composable
private fun PasswordVisibilityToggle(
    isVisible: Boolean,
    onToggle: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val icon = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff

    IconButton(
        onClick = onToggle,
        interactionSource = interactionSource,
        modifier = Modifier.size(24.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = if (isVisible) "Hide password" else "Show password",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PasswordStrengthIndicator(
    strength: PasswordStrength,
    requirements: List<Requirement>,
    modifier: Modifier = Modifier
) {
    val color = when (strength) {
        PasswordStrength.WEAK -> MaterialTheme.colorScheme.error
        PasswordStrength.MEDIUM -> MaterialTheme.colorScheme.primary
        PasswordStrength.STRONG -> MaterialTheme.colorScheme.tertiary
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Strength text and progress bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Password Strength: ",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = strength.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Animated progress bar
        LinearProgressIndicator(
            progress = { strength.strengthValue },
            color = color,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50))
        )

        // Password requirements checklist
        if (requirements.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            PasswordRequirementsChecklist(requirements = requirements)
        }
    }
}

@Composable
private fun PasswordRequirementsChecklist(requirements: List<Requirement>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        requirements.forEach { requirement ->
            RequirementItem(
                text = requirement.text,
                isMet = requirement.isMet
            )
        }
    }
}

@Composable
private fun RequirementItem(text: String, isMet: Boolean) {
    val icon = if (isMet) Icons.Default.CheckCircle else Icons.Default.Info
    val color =
        if (isMet) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = if (isMet) "Requirement met" else "Requirement not met",
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Composable
fun AuthButton(
    isLoading: Boolean,
    isRegistering: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(60.dp),
        enabled = !isLoading
    ) {
        AnimatedContent(
            targetState = isLoading,
        ) { loading ->
            if (loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = if (isRegistering) "Register" else "Login",
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun AuthAlternativeOptions(
    onAnonymousLogin: () -> Unit,
    onAuthModeToggle: () -> Unit,
    isRegistering: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(!isRegistering) {
            if (it) {
                TextButton(
                    onClick = onAnonymousLogin,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Continue as Guest",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Continue as Guest")
                }
            }
        }
        TextButton(onClick = onAuthModeToggle) {
            Text(
                text = if (isRegistering) "Already have an account? Sign In"
                else "Don't have an account? Register",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}