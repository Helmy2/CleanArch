package com.example.cleanarch.domain.repository

import com.example.cleanarch.domain.entity.DomainResult
import com.example.cleanarch.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUser(): Flow<DomainResult<User?>>
    suspend fun signInAnonymously(): DomainResult<Unit>
    suspend fun signOut(): DomainResult<Unit>
    suspend fun signInWithEmailAndPassword(
        email: String, password: String
    ): DomainResult<Unit>

    suspend fun convertToPermanentAccount(
        email: String, password: String, name: String
    ): DomainResult<Unit>

    suspend fun deleteUser(): DomainResult<Unit>
}