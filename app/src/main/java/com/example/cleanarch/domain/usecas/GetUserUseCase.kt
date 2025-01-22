package com.example.cleanarch.domain.usecas

import com.example.cleanarch.domain.entity.DomainResult
import com.example.cleanarch.domain.entity.User
import com.example.cleanarch.domain.exceptions.UserNotFoundException
import com.example.cleanarch.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.transform

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
        return userRepository.getUser(userId).transform { userData ->
            emit(DomainResult.Loading)
            if (userData != null) {
                emit(DomainResult.Success(userData))
            } else {
                emit(DomainResult.Failure(UserNotFoundException(userId)))
            }
        }.catch { e ->
            emit(DomainResult.Failure(e))
        }
    }
}