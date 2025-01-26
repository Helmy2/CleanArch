package com.example.domain.usecases

import com.example.domain.entity.Resource
import com.example.domain.entity.User
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow


class GetUserUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(): Flow<Resource<User?>> {
        return authRepository.getCurrentUser()
    }
}