package com.example.data.repository

import com.example.data.local.LocalAuthManager
import com.example.data.remote.RemoteAuthManager
import com.example.domain.entity.Resource
import com.example.domain.entity.User
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext


class AuthRepositoryImpl(
    private val remoteManager: RemoteAuthManager,
    private val localManager: LocalAuthManager,
    private val dispatcher: CoroutineDispatcher,
) : AuthRepository {

    override fun getCurrentUser(): Flow<Resource<User?>> {
        return flow<Resource<User?>> {
            // Emit the latest local user
            var localUser: User? = null
            localManager.getCurrentUser().collect { user ->
                localUser = user
                emit(Resource.Success(user))
            }

            // Emit remote user changes and update local storage if necessary
            remoteManager.getAuthState().collect { remoteUser ->
                if (remoteUser != null && remoteUser != localUser) {
                    // Save the remote user to local storage
                    localManager.saveUser(remoteUser)
                    emit(Resource.Success(remoteUser))
                } else {
                    // Clear local storage if the user signs out remotely
                    localManager.clearUser()
                    emit(Resource.Success(null))
                }
            }
        }.onStart { emit(Resource.Loading) }
            .catch {
                emit(Resource.Failure(it))
            }.flowOn(dispatcher)
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Unit> = withContext(dispatcher) {
        try {
            val user = remoteManager.signInWithEmailAndPassword(email, password)
            localManager.saveUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            val user = remoteManager.registerWithEmailAndPassword(email, password)
            localManager.saveUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInAnonymously(): Result<Unit> = withContext(dispatcher) {
        try {
            val user = remoteManager.signInAnonymously()
            localManager.saveUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun convertToPermanentAccount(
        email: String,
        password: String,
    ): Result<Unit> = withContext(dispatcher) {
        try {
            val user = remoteManager.linkToPermanentAccount(email, password)
            localManager.saveUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateDisplayName(name: String): Result<Unit> {
        return try {
            val user = remoteManager.updateDisplayName(name)
            localManager.saveUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            remoteManager.resetPassword(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(): Result<Unit> {
        return try {
            remoteManager.deleteUser()
            localManager.clearUser()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> = withContext(dispatcher) {
        try {
            remoteManager.signOut()
            localManager.clearUser()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}