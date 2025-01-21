package com.example.cleanarch.domain.repository

import com.example.cleanarch.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(userId: Int): Flow<User?>
}