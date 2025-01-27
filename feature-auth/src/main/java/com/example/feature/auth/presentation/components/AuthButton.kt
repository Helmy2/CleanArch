package com.example.feature.auth.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.core.theme.CleanArchTheme

@Composable
fun AuthButton(
    isLoading: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick, modifier = modifier, enabled = !isLoading
    ) {
        AnimatedContent(
            targetState = isLoading,
        ) { loading ->
            if (loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = text, modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun AuthButtonLoginPreview() {
    CleanArchTheme {
        Surface {
            AuthButton(
                isLoading = true,
                text = "Login",
                onClick = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
fun AuthButtonRegisterPreview() {
    CleanArchTheme {
        Surface {
            AuthButton(
                isLoading = false,
                text = "Login",
                onClick = {},
            )
        }
    }
}


