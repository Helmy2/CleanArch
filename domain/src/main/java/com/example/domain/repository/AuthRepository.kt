package com.example.domain.repository

import com.example.domain.entity.Resource
import com.example.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUser(): Flow<Resource<User?>>
    suspend fun signInAnonymously(): Result<Unit>
    suspend fun signOut(): Result<Unit>
    suspend fun signInWithEmailAndPassword(
        email: String, password: String
    ): Result<Unit>

    suspend fun registerWithEmailAndPassword(
        email: String, password: String
    ): Result<Unit>

    suspend fun convertToPermanentAccount(
        email: String, password: String
    ): Result<Unit>

    suspend fun deleteUser(): Result<Unit>

    suspend fun updateDisplayName(name: String): Result<Unit>

    suspend fun resetPassword(email: String): Result<Unit>
}