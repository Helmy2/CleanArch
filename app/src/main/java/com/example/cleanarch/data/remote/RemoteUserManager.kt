package com.example.cleanarch.data.remote

import com.example.cleanarch.domain.entity.User

interface RemoteUserManager {
    suspend fun fetchUser(userId: Int): User?
}