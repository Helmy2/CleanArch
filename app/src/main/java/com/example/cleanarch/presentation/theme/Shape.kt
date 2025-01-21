package com.example.cleanarch.presentation.theme

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

val shapes = Shapes(
    extraSmall = RoundedCornerShape(2.dp),
    small = RoundedCornerShape(4.dp),
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
                    .size(50.dp)
                    .clip(shape = shapes.extraSmall)
                    .background(color = colorScheme.primary)
            )
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(shape = shapes.small)
                    .background(color = colorScheme.primary)
            )
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(shape = shapes.medium)
                    .background(color = colorScheme.primary)
            )
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .clip(shape = shapes.large)
                    .background(color = colorScheme.primary)
            )
            Box(
                modifier = Modifier
                    .size(400.dp)
                    .clip(shape = shapes.extraLarge)
                    .background(color = colorScheme.primary)
            )
        }
    }
}
