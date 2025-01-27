package com.example.cleanarch.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.core.navigation.Destination
import com.example.core.navigation.TopLevelRoutes

@Composable
fun BottomNavBar(
    onClick: (destinations: Destination) -> Unit,
    currentRoute: String
) {
    NavigationBar {
        TopLevelRoutes.routes.forEach { topLevelRoute ->
            val isSelected =
                currentRoute == topLevelRoute.route.name
            NavigationBarItem(
                icon = {
                    Icon(
                        topLevelRoute.icon,
                        contentDescription = topLevelRoute.name,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                },
                label = {
                    Text(
                        text = topLevelRoute.name,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                },
                selected = isSelected,
                alwaysShowLabel = isSelected,
                onClick = {
                    onClick(topLevelRoute.route)
                }
            )
        }
    }
}