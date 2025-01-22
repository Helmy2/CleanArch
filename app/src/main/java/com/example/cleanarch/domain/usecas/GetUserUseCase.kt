package com.example.cleanarch.domain.usecas

import com.example.cleanarch.domain.entity.DomainResult
import com.example.cleanarch.domain.entity.User
import com.example.cleanarch.domain.exceptions.UserNotFoundException
import com.example.cleanarch.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * A use case class responsible for retrieving a user by their ID.
 *
 * This class interacts with a [UserRepository] to fetch user data and
 * handles potential errors, emitting results through a [Flow].
 *
 * @property userRepository The repository responsible for user data access.
 */
class GetUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke(userId: Int): Flow<DomainResult<User>> {
        return userRepository.getUser(userId).map { userData ->
            if (userData != null) {
                DomainResult.Success(userData)
            } else {
                DomainResult.Failure(UserNotFoundException(userId))
            }
        }.onStart { emit(DomainResult.Loading) }.catch { e ->
            emit(DomainResult.Failure(e))
        }
    }
}