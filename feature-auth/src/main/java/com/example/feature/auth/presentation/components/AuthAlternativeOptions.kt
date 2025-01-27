package com.example.feature.auth.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.core.theme.CleanArchTheme
import com.example.feature.auth.R
import com.example.feature.auth.presentation.AuthMode


@Composable
fun AuthAlternativeOptions(
    onAnonymousLogin: () -> Unit,
    onAuthModeToggle: (AuthMode) -> Unit,
    authMode: AuthMode,
    modifier: Modifier = Modifier
) {
    AnimatedContent(authMode) { mode ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth(),
        ) {
            if (mode == AuthMode.Login) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onAnonymousLogin() }
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = stringResource(R.string.continue_as_guest),
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        stringResource(R.string.continue_as_guest),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clip(MaterialTheme.shapes.small)
                    .clickable {
                        when {
                            mode == AuthMode.Login -> onAuthModeToggle(AuthMode.Register)
                            else -> onAuthModeToggle(AuthMode.Login)
                        }
                    }
                    .padding(8.dp),
            ) {
                Text(
                    text = when (mode) {
                        AuthMode.Login -> stringResource(R.string.no_account)
                        AuthMode.Register -> stringResource(R.string.already_have_account)
                        AuthMode.ForgotPassword -> stringResource(R.string.back_to_login)
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
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
                authMode = AuthMode.Login
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
                authMode = AuthMode.Register
            )
        }
    }
}

@PreviewLightDark
@Composable
fun AuthAlternativeOptionsForgotPasswordPreview() {
    CleanArchTheme {
        Surface {
            AuthAlternativeOptions(
                onAnonymousLogin = {},
                onAuthModeToggle = {},
                authMode = AuthMode.ForgotPassword
            )
        }
    }
}