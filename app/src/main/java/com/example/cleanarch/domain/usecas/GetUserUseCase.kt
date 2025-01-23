package com.example.cleanarch.domain.usecas

import com.example.cleanarch.domain.entity.DomainResult
import com.example.cleanarch.domain.entity.User
import com.example.cleanarch.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow


class GetUserUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(): Flow<DomainResult<User?>> {
        return authRepository.getCurrentUser()
    }
}