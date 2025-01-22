package com.example.cleanarch.data.repository

import com.example.cleanarch.domain.entity.User
import com.example.cleanarch.domain.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Implementation of [UserRepository] responsible for fetching user data.
 *
 * This class provides a concrete implementation of the [UserRepository] interface,
 * simulating data retrieval from a network or database source.
 */
class UserRepositoryImpl : UserRepository {
    /**
     * Retrieves a user by their ID.
     *
     * This function simulates fetching a user from a data source (e.g., network or database).
     * It introduces a delay to mimic real-world latency.
     *
     * @param userId The ID of the user to retrieve.
     * @return A Flow emitting the user if found, or null if not found.
     */
    override fun getUser(userId: Int): Flow<User?> {
        return flow {
            // Simulate a network call or database query
            delay(1000) // Simulate delay
            if (userId == 1) {
                emit(User(userId, "John Doe", "john.doe@example.com"))
            } else {
                emit(null) // User not found
            }
        }
    }
}