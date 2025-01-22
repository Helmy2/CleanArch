package com.example.cleanarch.domain.exceptions

/**
 * Exception thrown when a user with a specific ID is not found.
 *
 * @param userId The ID of the user that was not found.
 * @constructor Creates a new UserNotFoundException with the specified user ID.
 * @property message The error message associated with this exception, indicating that a user with the given ID was not found.
 *
 * This exception is typically used when an operation that requires a specific user
 * cannot locate the user in the data store. It provides a clear indication that the
 * user data could not be found and includes the user ID for context.
 */
class UserNotFoundException(userId: Int) : Exception("User with ID $userId not found")