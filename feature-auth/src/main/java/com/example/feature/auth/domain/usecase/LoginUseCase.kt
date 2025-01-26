package com.example.feature.auth.domain.usecase

import com.example.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.signInWithEmailAndPassword(email, password)
    }
}