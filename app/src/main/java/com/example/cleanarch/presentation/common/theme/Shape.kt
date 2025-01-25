package com.example.cleanarch.presentation.common.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(16.dp)
)

@Preview
@Composable
private fun PreviewShapes() {
    CleanArchTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(shape = Shapes.extraSmall)
                    .background(color = colorScheme.primary)
            )
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(shape = Shapes.small)
                    .background(color = colorScheme.primary)
            )
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(shape = Shapes.medium)
                    .background(color = colorScheme.primary)
            )
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(shape = Shapes.large)
                    .background(color = colorScheme.primary)
            )
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(shape = Shapes.extraLarge)
                    .background(color = colorScheme.primary)
            )
        }
    }
}
