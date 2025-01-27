package com.example.feature.auth.presentation.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.core.theme.CleanArchTheme
import com.example.feature.auth.R
import com.example.feature.auth.presentation.PasswordStrength
import com.example.feature.auth.presentation.Requirement


@Composable
fun AuthPasswordField(
    value: String,
    @StringRes error: Int?,
    isVisible: Boolean,
    isRegistering: Boolean,
    passwordStrength: PasswordStrength,
    passwordRequirements: List<Requirement>,
    keyboardOptions: KeyboardOptions,
    onValueChange: (String) -> Unit,
    onVisibilityToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(value = value,
        onValueChange = onValueChange,
        placeholder = { Text(stringResource(R.string.password)) },
        keyboardOptions = keyboardOptions,
        visualTransformation = if (isVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        singleLine = true,
        isError = error != null,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f),
            unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f),
        ),
        trailingIcon = {
            PasswordVisibilityToggle(
                isVisible = isVisible, onToggle = onVisibilityToggle
            )
        },
        supportingText = {
            AnimatedContent(error != null) {
                if (it) {
                    Text(
                        text = if (error != null) stringResource(error) else "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                } else {
                    AnimatedVisibility(isRegistering && passwordStrength != PasswordStrength.STRONG && value.isNotEmpty()) {
                        PasswordStrengthIndicator(
                            strength = passwordStrength,
                            requirements = passwordRequirements,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        })
}

@PreviewLightDark
@Composable
fun AuthPasswordFieldRequirementsPreview() {
    CleanArchTheme {
        Surface {
            AuthPasswordField(
                value = "password123",
                error = null,
                isVisible = true,
                isRegistering = true,
                passwordStrength = PasswordStrength.WEAK,
                passwordRequirements = listOf(
                    Requirement(R.string.password_requirement_length, true),
                    Requirement(R.string.password_requirement_uppercase, false),
                    Requirement(R.string.password_requirement_special, false)
                ),
                keyboardOptions = KeyboardOptions.Default,
                onValueChange = {},
                onVisibilityToggle = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
fun AuthPasswordFieldErrorPreview() {
    CleanArchTheme {
        Surface {
            AuthPasswordField(value = "password",
                error = R.string.error_invalid_password,
                isVisible = true,
                isRegistering = false,
                passwordStrength = PasswordStrength.WEAK,
                passwordRequirements = emptyList(),
                keyboardOptions = KeyboardOptions.Default,
                onValueChange = {},
                onVisibilityToggle = {})
        }
    }
}

@PreviewLightDark
@Composable
fun AuthPasswordFieldEmptyPreview() {
    CleanArchTheme {
        Surface {
            AuthPasswordField(value = "",
                error = null,
                isVisible = true,
                isRegistering = false,
                passwordStrength = PasswordStrength.WEAK,
                passwordRequirements = emptyList(),
                keyboardOptions = KeyboardOptions.Default,
                onValueChange = {},
                onVisibilityToggle = {})
        }
    }
}

@Composable
private fun PasswordVisibilityToggle(
    isVisible: Boolean, onToggle: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val icon = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff

    IconButton(
        onClick = onToggle, interactionSource = interactionSource, modifier = Modifier.size(24.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = if (isVisible) stringResource(R.string.hide_password) else stringResource(
                R.string.show_password
            ),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PasswordStrengthIndicator(
    strength: PasswordStrength, requirements: List<Requirement>, modifier: Modifier = Modifier
) {
    val color = when (strength) {
        PasswordStrength.WEAK -> MaterialTheme.colorScheme.error
        PasswordStrength.MEDIUM -> MaterialTheme.colorScheme.primary
        PasswordStrength.STRONG -> MaterialTheme.colorScheme.tertiary
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Strength text and progress bar
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
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
                text = requirement.text, isMet = requirement.isMet
            )
        }
    }
}

@Composable
private fun RequirementItem(@StringRes text: Int, isMet: Boolean) {
    val icon = if (isMet) Icons.Default.CheckCircle else Icons.Default.Info
    val color =
        if (isMet) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = if (isMet) stringResource(R.string.requirement_met)
            else stringResource(
                R.string.requirement_not_met
            ),
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(text), style = MaterialTheme.typography.labelSmall, color = color
        )
    }
}

