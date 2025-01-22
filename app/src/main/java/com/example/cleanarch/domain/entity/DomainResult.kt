package com.example.cleanarch.domain.entity

/**
 * Represents the result of a domain operation, encapsulating either a successful outcome,
 * an error, or a loading state.
 *
 * This sealed class is used to handle different outcomes of a domain operation in a structured and type-safe way.
 * It helps in managing the flow of data and potential errors in a clean and predictable manner.
 *
 * @param T The type of data contained in a successful result.
 */
sealed class DomainResult<out T> {
    /**
     * Represents a successful result in a domain operation.
     *
     * This class indicates that the operation completed successfully and provides
     * the resulting [data].
     *
     * @param T The type of data returned by the successful operation.
     * @property data The data associated with the successful operation.
     *
     * Example:
     * ```
     * val result: DomainResult<Int> = Success(10)
     * ```
     */
    data class Success<out T>(val data: T) : DomainResult<T>()


    /**
     * Represents a failure result in a domain operation.
     *
     * This class encapsulates an exception that occurred during the execution
     * of a domain operation, indicating that the operation was not successful.
     *
     * @property exception The [Throwable] representing the exception that caused the failure.
     *
     * @see DomainResult
     */
    data class Failure(val exception: Throwable) : DomainResult<Nothing>()

    /**
     * Represents a loading state in a domain operation.
     *
     * This object signifies that a domain operation is currently in progress but has not yet
     * completed or failed. It is a specific type of [DomainResult] used to indicate this state.
     *
     * This object does not carry any data, it simply represents the "loading" state.
     */
    object Loading : DomainResult<Nothing>()
}