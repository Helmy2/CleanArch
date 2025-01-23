package com.example.cleanarch.data.local

import com.example.cleanarch.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface LocalAuthManager {
    suspend fun saveUser(user: User)
    fun getCurrentUser(): Flow<User?>
    suspend fun clearUser()
}