package com.example.cleanarch.data.remote

import com.example.cleanarch.domain.entity.User
import com.example.cleanarch.domain.exceptions.AuthException.AccountConversionFailedException
import com.example.cleanarch.domain.exceptions.AuthException.AnonymousSignInFailedException
import com.example.cleanarch.domain.exceptions.AuthException.UserNotFoundException
import com.example.cleanarch.domain.exceptions.ExceptionMapper
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await

class RemoteAuthManagerImpl(
    private val firebaseAuth: FirebaseAuth,
    private val exceptionMapper: ExceptionMapper,
) : RemoteAuthManager {

    override fun getAuthState(): Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toDomainUser())
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }.catch {
        throw exceptionMapper.map(it)
    }


    override suspend fun signInWithEmailAndPassword(email: String, password: String): User {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.toDomainUser() ?: throw UserNotFoundException()
        } catch (e: Exception) {
            throw exceptionMapper.map(e)
        }
    }

    override suspend fun signInAnonymously(): User {
        return try {
            val result = firebaseAuth.signInAnonymously().await()
            result.user?.toDomainUser() ?: throw AnonymousSignInFailedException()
        } catch (e: Exception) {
            throw exceptionMapper.map(e)
        }
    }

    override suspend fun linkToPermanentAccount(
        email: String,
        password: String,
        name: String
    ): User {
        val user = firebaseAuth.currentUser ?: throw UserNotFoundException()

        return try {
            // 1. Link email/password credential
            val credential = EmailAuthProvider.getCredential(email, password)
            val authResult = user.linkWithCredential(credential).await()

            // 2. Update profile with name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            authResult.user?.updateProfile(profileUpdates)?.await()

            authResult.user?.toDomainUser() ?: throw AccountConversionFailedException()
        } catch (e: Exception) {
            throw exceptionMapper.map(e)
        }
    }


    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    private fun FirebaseUser.toDomainUser(): User = User(
        id = uid,
        name = displayName ?: "Anonymous",
        email = email ?: "",
        isAnonymous = isAnonymous
    )

    override suspend fun deleteUser() {
        val user = firebaseAuth.currentUser ?: throw UserNotFoundException()

        try {
            user.delete().await()
        } catch (e: Exception) {
            throw exceptionMapper.map(e)
        }
    }
}