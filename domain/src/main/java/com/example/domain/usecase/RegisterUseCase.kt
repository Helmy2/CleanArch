package com.example.domain.usecase

import com.example.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        email: String, password: String, name: String
    ): Result<Unit> {
        val registerResult = authRepository.registerWithEmailAndPassword(email, password)
        if (registerResult.isFailure) return registerResult

        val updateResult = authRepository.updateDisplayName(name)
        return updateResult
    }
}