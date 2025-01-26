package com.example.domain.usecases

import com.example.domain.repository.AuthRepository

class SignInAnonymouslyUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.signInAnonymously()
    }
}