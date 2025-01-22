package com.example.cleanarch.data.local

import com.example.cleanarch.domain.entity.User

interface LocalUserManager {
    suspend fun saveUser(user: User)
    suspend fun getUser(userId: Int): User?
}