package com.example.feature.auth.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.core.theme.CleanArchTheme

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
                focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f),
                unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f),
            ),
            supportingText = when {
                error != null -> {
                    {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                else -> null
            }
        )
    }
}

@PreviewLightDark
@Composable
fun AuthTextFieldPreview() {
    CleanArchTheme {
        Surface {
            AuthTextField(
                value = "ads",
                label = "Email",
                error = "Invalid email format",
                keyboardOptions = KeyboardOptions.Default,
                onValueChange = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
fun AuthTextFieldErrorPreview() {
    CleanArchTheme {
        Surface {
            AuthTextField(
                value = "John Doe",
                label = "Name",
                error = null,
                keyboardOptions = KeyboardOptions.Default,
                onValueChange = {},
            )
        }
    }
}