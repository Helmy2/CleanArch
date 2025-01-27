package com.example.domain.usecase

import com.example.domain.repository.AuthRepository

class RestPasswordUseCase(private val authRepository: AuthRepository){
    suspend operator fun invoke(email: String): Result<Unit> {
        return authRepository.resetPassword(email)
    }
}