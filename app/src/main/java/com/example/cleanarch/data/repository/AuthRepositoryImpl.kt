package com.example.cleanarch.data.repository

import com.example.cleanarch.data.local.LocalAuthManager
import com.example.cleanarch.data.remote.RemoteAuthManager
import com.example.cleanarch.domain.entity.DomainResult
import com.example.cleanarch.domain.entity.User
import com.example.cleanarch.domain.repository.AuthRepository
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

    override fun getCurrentUser(): Flow<DomainResult<User?>> {
        return flow<DomainResult<User?>> {
            // Emit the latest local user
            var localUser: User? = null
            localManager.getCurrentUser().collect { user ->
                localUser = user
                emit(DomainResult.Success(user))
            }

            // Emit remote user changes and update local storage if necessary
            remoteManager.getAuthState().collect { remoteUser ->
                if (remoteUser != null && remoteUser != localUser) {
                    // Save the remote user to local storage
                    localManager.saveUser(remoteUser)
//                    emit(DomainResult.Success(remoteUser))
                } else {
                    // Clear local storage if the user signs out remotely
                    localManager.clearUser()
//                    emit(DomainResult.Success(null))
                }
            }
        }.onStart { emit(DomainResult.Loading) }
            .catch {
                emit(DomainResult.Failure(it))
            }.flowOn(dispatcher)
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ) = withContext(dispatcher) {
        try {
            val user = remoteManager.signInWithEmailAndPassword(email, password)
            localManager.saveUser(user)
            DomainResult.Success(Unit)
        } catch (e: Exception) {
            DomainResult.Failure(e)
        }
    }

    override suspend fun signInAnonymously(): DomainResult<Unit> = withContext(dispatcher) {
        try {
            val user = remoteManager.signInAnonymously()
            localManager.saveUser(user)
            DomainResult.Success(Unit)
        } catch (e: Exception) {
            DomainResult.Failure(e)
        }
    }

    override suspend fun convertToPermanentAccount(
        email: String,
        password: String,
        name: String
    ): DomainResult<Unit> = withContext(dispatcher) {
        try {
            val user = remoteManager.linkToPermanentAccount(email, password, name)
            localManager.saveUser(user)
            DomainResult.Success(Unit)
        } catch (e: Exception) {
            DomainResult.Failure(e)
        }
    }

    override suspend fun deleteUser(): DomainResult<Unit> {
        return try {
            remoteManager.deleteUser()
            localManager.clearUser()
            DomainResult.Success(Unit)
        } catch (e: Exception) {
            DomainResult.Failure(e)
        }
    }

    override suspend fun signOut(): DomainResult<Unit> = withContext(dispatcher) {
        try {
            remoteManager.signOut()
            localManager.clearUser()
            DomainResult.Success(Unit)
        } catch (e: Exception) {
            DomainResult.Failure(e)
        }
    }
}