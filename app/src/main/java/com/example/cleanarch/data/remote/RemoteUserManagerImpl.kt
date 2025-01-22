package com.example.cleanarch.data.remote

import com.example.cleanarch.domain.entity.User
import kotlinx.coroutines.delay

class RemoteUserManagerImpl : RemoteUserManager {
    private val userList = listOf(
        User(1, "John Doe", "john.doe@example.com"),
        User(2, "Jane Smith", "jane.smith@example.com"),
        User(3, "Bob Johnson", "bob.johnson@example.com"),
        User(4, "Alice Brown", "alice.brown@example.com"),
        User(5, "Charlie White", "charlie.white@example.com"),
        User(6, "Eva Green", "eva.green@example.com"),
        User(7, "David Black", "david.black@example.com"),
    )

    override suspend fun fetchUser(userId: Int): User? {
        // Simulate network call
        delay(1000)

        return userList.find { it.id == userId }
    }
}