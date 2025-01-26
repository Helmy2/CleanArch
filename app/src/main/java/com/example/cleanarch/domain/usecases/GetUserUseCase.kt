package com.example.cleanarch.domain.usecases

import com.example.cleanarch.domain.entity.Resource
import com.example.cleanarch.domain.entity.User
import com.example.cleanarch.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow


class GetUserUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(): Flow<Resource<User?>> {
        return authRepository.getCurrentUser()
    }
}