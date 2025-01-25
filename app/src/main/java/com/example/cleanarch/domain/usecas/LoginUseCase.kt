package com.example.cleanarch.domain.usecas

import com.example.cleanarch.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.signInWithEmailAndPassword(email, password)
    }
}