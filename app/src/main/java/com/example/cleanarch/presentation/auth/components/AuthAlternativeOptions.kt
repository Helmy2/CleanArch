package com.example.cleanarch.presentation.auth.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.cleanarch.presentation.common.theme.CleanArchTheme


@Composable
fun AuthAlternativeOptions(
    onAnonymousLogin: () -> Unit,
    onAuthModeToggle: () -> Unit,
    isRegistering: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(!isRegistering) {
            if (it) {
                TextButton(
                    onClick = onAnonymousLogin
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
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isRegistering) "Already have an account? " else "Don't have an account? ",
                style = MaterialTheme.typography.bodyMedium
            )
            TextButton(onClick = onAuthModeToggle) {
                Text(
                    text = if (isRegistering) "Login" else "Register",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun AuthAlternativeOptionsLoginPreview() {
    CleanArchTheme {
        Surface {
            AuthAlternativeOptions(
                onAnonymousLogin = {},
                onAuthModeToggle = {},
                isRegistering = false
            )
        }
    }
}

@PreviewLightDark
@Composable
fun AuthAlternativeOptionsRegisterPreview() {
    CleanArchTheme {
        Surface {
            AuthAlternativeOptions(
                onAnonymousLogin = {},
                onAuthModeToggle = {},
                isRegistering = true
            )
        }
    }
}