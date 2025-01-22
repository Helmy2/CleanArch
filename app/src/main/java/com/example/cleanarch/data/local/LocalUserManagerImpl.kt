package com.example.cleanarch.data.local

import com.example.cleanarch.domain.entity.User

class LocalUserManagerImpl : LocalUserManager {
    private val cache = mutableMapOf<Int, User>()

    override suspend fun saveUser(user: User) {
        cache[user.id] = user
    }

    override suspend fun getUser(userId: Int): User? {
        return cache[userId]
    }
}