package com.example.cleanarch.data.repository

import com.example.cleanarch.data.local.LocalUserManager
import com.example.cleanarch.data.remote.RemoteUserManager
import com.example.cleanarch.domain.entity.User
import com.example.cleanarch.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class UserRepositoryImpl(
    private val remoteManager: RemoteUserManager,
    private val localManager: LocalUserManager
) : UserRepository {
    override fun getUser(userId: Int): Flow<User?> {
        return flow {
            val localUser = localManager.getUser(userId)
            if (localUser != null) {
                emit(localUser)
            } else {
                val remoteUser = remoteManager.fetchUser(userId)
                if (remoteUser != null) {
                    localManager.saveUser(remoteUser)
                }
                emit(remoteUser)
            }
        }
    }
}