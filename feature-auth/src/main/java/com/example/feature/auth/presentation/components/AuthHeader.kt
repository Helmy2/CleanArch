package com.example.feature.auth.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.core.theme.CleanArchTheme
import com.example.feature.auth.R
import com.example.feature.auth.presentation.AuthMode

@Composable
fun AuthHeader(authMode: AuthMode, modifier: Modifier = Modifier) {
    AnimatedContent(
        targetState = authMode,
        modifier = modifier
    ) { mode ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(
                        when (mode) {
                            AuthMode.ForgotPassword -> R.drawable.forgot_password_back
                            AuthMode.Login -> R.drawable.welcome_back
                            AuthMode.Register -> R.drawable.create_back
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
                Icon(
                    painter = painterResource(
                        when (mode) {
                            AuthMode.ForgotPassword -> R.drawable.forgot_password_front
                            AuthMode.Login -> R.drawable.welcome_front
                            AuthMode.Register -> R.drawable.create_front
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = MaterialTheme.colorScheme.primary
                )
            }


            Spacer(Modifier.height(8.dp))

            Text(
                text = when (mode) {
                    AuthMode.Login -> stringResource(R.string.welcome_back)
                    AuthMode.Register -> stringResource(R.string.create_account)
                    AuthMode.ForgotPassword -> stringResource(R.string.rest_password)
                },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.animateContentSize()
            )

            Crossfade(targetState = mode) {
                Text(
                    text = when (it) {
                        AuthMode.Login -> stringResource(R.string.login_to_your_account)
                        AuthMode.Register -> stringResource(R.string.no_account)
                        AuthMode.ForgotPassword -> stringResource(R.string.enter_email_reset_password)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun AuthHeaderPreview_Register() {
    CleanArchTheme {
        Surface {
            AuthHeader(AuthMode.Register, Modifier.height(300.dp))
        }
    }
}

@PreviewLightDark
@Composable
fun AuthHeaderPreview_Login() {
    CleanArchTheme {
        Surface {
            AuthHeader(AuthMode.Login, Modifier.height(300.dp))
        }
    }
}

@PreviewLightDark
@Composable
fun AuthHeaderPreview_ForgotPassword() {
    CleanArchTheme {
        Surface {
            AuthHeader(AuthMode.ForgotPassword, Modifier.height(300.dp))
        }
    }
}

