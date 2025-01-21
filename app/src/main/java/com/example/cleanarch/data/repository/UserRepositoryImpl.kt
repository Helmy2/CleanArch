package com.example.cleanarch.data.repository

import com.example.cleanarch.domain.entity.User
import com.example.cleanarch.domain.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl : UserRepository {
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