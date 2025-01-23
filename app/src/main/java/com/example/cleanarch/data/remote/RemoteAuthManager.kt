package com.example.cleanarch.data.remote

import com.example.cleanarch.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface RemoteAuthManager {
    fun getAuthState(): Flow<User?>
    suspend fun signInAnonymously(): User
    suspend fun signInWithEmailAndPassword(email: String, password: String): User
    suspend fun linkToPermanentAccount(email: String, password: String, name: String): User
    suspend fun signOut()
    suspend fun deleteUser()
}