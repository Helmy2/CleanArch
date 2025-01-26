package com.example.data.remote

import com.example.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface RemoteAuthManager {
    fun getAuthState(): Flow<User?>
    suspend fun signInAnonymously(): User
    suspend fun signInWithEmailAndPassword(email: String, password: String): User
    suspend fun registerWithEmailAndPassword(email: String, password: String): User
    suspend fun linkToPermanentAccount(email: String, password: String): User
    suspend fun updateDisplayName(string: String): User
    suspend fun signOut()
    suspend fun deleteUser()
}