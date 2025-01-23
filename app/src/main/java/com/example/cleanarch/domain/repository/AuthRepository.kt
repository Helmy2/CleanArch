package com.example.cleanarch.domain.repository

import com.example.cleanarch.domain.entity.Resource
import com.example.cleanarch.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUser(): Flow<Resource<User?>>
    suspend fun signInAnonymously(): Resource<Unit>
    suspend fun signOut(): Resource<Unit>
    suspend fun signInWithEmailAndPassword(
        email: String, password: String
    ): Resource<Unit>

    suspend fun convertToPermanentAccount(
        email: String, password: String, name: String
    ): Resource<Unit>

    suspend fun deleteUser(): Resource<Unit>
}