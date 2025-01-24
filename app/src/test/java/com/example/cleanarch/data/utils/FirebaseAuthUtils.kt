package com.example.cleanarch.data.utils

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk


fun mockFirebaseAuthException() = mockk<FirebaseAuthException>(relaxed = true) {
    every { errorCode } returns "code"
    every { message } returns "message"
}

/**
 * Creates a mock instance of [FirebaseUser] for testing purposes.
 *
 * This function simplifies the creation of mock FirebaseUser objects with customizable properties.
 * It utilizes MockK to create a mock object and set up predefined responses for common FirebaseUser methods.
 *
 * @param userId The unique user identifier (UID) to be returned by the mock FirebaseUser.
 * @param email The user's email address to be returned by the mock FirebaseUser.
 * @param isAnonymous Indicates whether the user is anonymous. Defaults to `true`.
 * @param displayName The user's display name. Defaults to `null`.
 * @return A mocked [FirebaseUser] instance with the specified properties.
 *
 * Example usage:
 * ```kotlin
 * // Create a mock anonymous user with a specific UID
 * val anonymousUser = mockFirebaseUser("some_user_id")
 *
 * // Create a mock non-anonymous user with an email and display name
 * val registeredUser = mockFirebaseUser(
 *     userId = "user123",
 *     email = "test@example.com",
 *     isAnonymous = false,
 *     displayName = "Test User"
 * )
 * ```
 */
fun mockFirebaseUser(
    userId: String, email: String, isAnonymous: Boolean = true, displayName: String? = null
): FirebaseUser {
    val mockUser = mockk<FirebaseUser>()
    every { mockUser.uid } returns userId
    every { mockUser.email } returns email
    every { mockUser.isAnonymous } returns isAnonymous
    every { mockUser.displayName } returns displayName
    return mockUser
}


/**
 * Creates a mocked [AuthResult] object for testing purposes.
 *
 * This function creates a mock instance of `AuthResult` using MockK and
 * configures it to return the provided `FirebaseUser` when the `user`
 * property is accessed. This is useful for simulating the results of
 * Firebase authentication operations in unit or integration tests without
 * interacting with the real Firebase service.
 *
 * @param user The `FirebaseUser` object that should be returned when
 *             `authResult.user` is accessed. If `null`, accessing `authResult.user` will return null.
 * @return A mocked `AuthResult` object configured to return the given user.
 */
fun mockAuthResult(
    user: FirebaseUser?
): AuthResult {
    val authResult = mockk<AuthResult>()
    every { authResult.user } returns user
    return authResult
}

/**
 * Creates a mocked [Task] instance for testing purposes.
 *
 * This function simplifies the creation of a mocked [Task] object, allowing you to
 * specify the desired result or exception that the task should represent. The task is
 * pre-configured to be complete, not canceled, and its success status is determined by
 * whether an exception is provided.
 *
 * @param result The result the task should return upon successful completion. Defaults to null.
 * @param exception The exception the task should return if it failed. Defaults to null, indicating success.
 * @return A mocked [Task] instance configured with the specified result and exception.
 *
 * @param T The type of the result that the task will produce.
 *
 * Example Usage:
 *
 * ```kotlin
 * // Create a successful task with an integer result.
 * val successfulTask = mockTask(result = 10)
 *
 * // Create a task that failed with a specific exception.
 * val failedTask = mockTask<String>(exception = RuntimeException("Something went wrong"))
 *
 * // Create a successful task that returns null
 * val successfulNullTask = mockTask<String>(result = null)
 *
 * // Create a successful task that returns a string
 * val successfulStringTask = mockTask<String>(result = "success")
 * ```
 */
inline fun <reified T> mockTask(
    result: T? = null, exception: Exception? = null
): Task<T> {
    require(result == null || exception == null) { "Cannot provide both a result and an exception." }

    val task = mockk<Task<T>>()
    every { task.isComplete } returns true
    every { task.isCanceled } returns false
    every { task.isSuccessful } returns (exception == null)
    every { task.result } returns result
    every { task.exception } returns exception
    return task
}


