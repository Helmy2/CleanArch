package com.example.cleanarch.presentation.auth.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.cleanarch.R
import com.example.core.theme.CleanArchTheme

@Composable
fun AuthHeader(isRegistering: Boolean) {
    AnimatedContent(
        targetState = isRegistering,
    ) { registering ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.size(200.dp)
            ) {
                Image(
                    painter = painterResource(if (registering) R.drawable.create_back else R.drawable.welcome_black),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
                Icon(
                    painter = painterResource(if (registering) R.drawable.create_front else R.drawable.welcome_front),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = MaterialTheme.colorScheme.primary
                )
            }


            Spacer(Modifier.height(8.dp))

            Text(
                text = if (registering) "Create Account" else "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.animateContentSize()
            )

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

@PreviewLightDark
@Composable
fun AuthHeaderPreview_Register() {
    CleanArchTheme {
        Surface {
            AuthHeader(isRegistering = true)
        }
    }
}

@PreviewLightDark
@Composable
fun AuthHeaderPreview_Login() {
    CleanArchTheme {
        Surface {
            AuthHeader(isRegistering = false)
        }
    }
}
