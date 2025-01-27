package com.example.core.navigation

import kotlinx.serialization.Serializable

sealed class Destination {
    val name: String get() = this::class.simpleName ?: "unknown"

    @Serializable
    data object Home : Destination()

    @Serializable
    data object Auth : Destination()

    @Serializable
    data object Profile : Destination()

    @Serializable
    data class Details(val id: String) : Destination()
}
