package com.example.cleanarch.domain.entity

/**
 * Represents a user with a unique identifier, name, and email address.
 *
 * @property id The unique identifier of the user.
 * @property name The name of the user.
 * @property email The email address of the user.
 *
 * @constructor Creates a new User object with the specified properties.
 */
data class User(
    val id: Int, val name: String, val email: String
)