package com.example.cleanarch.domain.usecas

import com.example.cleanarch.domain.repository.AuthRepository

class SignInAnonymouslyUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.signInAnonymously()
    }
}