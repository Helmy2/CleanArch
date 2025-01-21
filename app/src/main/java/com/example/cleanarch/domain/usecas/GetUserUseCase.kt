package com.example.cleanarch.domain.usecas

import com.example.cleanarch.domain.entity.User
import com.example.cleanarch.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import com.example.cleanarch.domain.entity.DomainResult

class GetUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke(userId: Int): Flow<DomainResult<User>> {
        return userRepository.getUser(userId).map { user ->
            if (user != null) {
                DomainResult.Success(user)
            } else {
                DomainResult.Error(Throwable("User not found"))
            }
        }.catch { e ->
            emit(DomainResult.Error(e))
        }.onStart {
            emit(DomainResult.Loading)
        }
    }
}