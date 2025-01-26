package com.example.data.remote

import com.example.data.exception.AuthException.AccountConversionFailedException
import com.example.data.exception.AuthException.AnonymousSignInFailedException
import com.example.data.exception.AuthException.UserNotFoundException
import com.example.domain.entity.User
import com.example.domain.exceptions.ExceptionMapper
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
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

    override suspend fun registerWithEmailAndPassword(email: String, password: String): User {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
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
    ): User {
        val user = firebaseAuth.currentUser ?: throw UserNotFoundException()

        return try {
            val credential = EmailAuthProvider.getCredential(email, password)
            val authResult = user.linkWithCredential(credential).await()

            authResult.user?.toDomainUser() ?: throw AccountConversionFailedException()
        } catch (e: Exception) {
            throw exceptionMapper.map(e)
        }
    }

    override suspend fun updateDisplayName(string: String): User {
        val user = firebaseAuth.currentUser ?: throw UserNotFoundException()
        val profileUpdates = userProfileChangeRequest {
            displayName = string
        }
        try {
            user.updateProfile(profileUpdates).await()
            return user.toDomainUser()
        } catch (e: Exception) {
            throw exceptionMapper.map(e)
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun deleteUser() {
        val user = firebaseAuth.currentUser ?: throw UserNotFoundException()

        try {
            user.delete().await()
        } catch (e: Exception) {
            throw exceptionMapper.map(e)
        }
    }

    private fun FirebaseUser.toDomainUser(): User = User(
        id = uid,
        name = displayName ?: "Anonymous",
        email = email ?: "",
        isAnonymous = isAnonymous
    )
}