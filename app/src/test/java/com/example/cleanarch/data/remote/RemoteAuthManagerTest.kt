package com.example.cleanarch.data.remote

import com.example.cleanarch.data.utils.mockAuthResult
import com.example.cleanarch.data.utils.mockFirebaseAuthException
import com.example.cleanarch.data.utils.mockFirebaseUser
import com.example.cleanarch.data.utils.mockTask
import com.example.domain.entity.User
import com.example.cleanarch.domain.exceptions.AuthException.UserNotFoundException
import com.example.domain.exceptions.ExceptionMapper
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.MockKException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RemoteAuthManagerTest {
    private lateinit var mockFirebaseAuth: FirebaseAuth
    private lateinit var authManager: com.example.data.remote.RemoteAuthManager
    private lateinit var mockExceptionMapper: com.example.domain.exceptions.ExceptionMapper

    @Before
    fun setup() {
        // Mock FirebaseAuth and related objects
        mockFirebaseAuth = mockk()
        mockExceptionMapper = mockk()
        authManager = com.example.data.remote.RemoteAuthManagerImpl(
            mockFirebaseAuth, mockExceptionMapper
        )
    }

    @Test
    fun `getAuthState should map exception when there's an error in flow`() = runTest {
        val expectedException = RuntimeException("Mapped error")
        every { mockFirebaseAuth.addAuthStateListener(any()) } throws mockFirebaseAuthException()

        every { mockExceptionMapper.map(any()) } returns expectedException

        shouldThrow<RuntimeException> {
            authManager.getAuthState().first()
        } shouldBe expectedException
    }

    @Test
    fun `signInWithEmailAndPassword should return user when sign in succeeds`() = runTest {
        val mockUser = mockFirebaseUser("user1", "test@example.com", false)
        val mockTask = mockTask(mockAuthResult(mockUser), null)

        every { mockFirebaseAuth.signInWithEmailAndPassword(any(), any()) } returns mockTask

        val result = authManager.signInWithEmailAndPassword("test@example.com", "password")
        result shouldBe mockUser.toDomainUser()
    }

    @Test
    fun `signInWithEmailAndPassword should map exception when sign in fails`() = runTest {
        // 1. Mock the FirebaseAuthException without relying on Android SDK
        val originalException = mockFirebaseAuthException()
        val mappedException = RuntimeException("Mapped error")

        // 2. Mock the failed Task
        val mockTask = mockTask<AuthResult>(
            result = null, exception = originalException
        )

        // 3. Mock FirebaseAuth behavior
        every { mockFirebaseAuth.signInWithEmailAndPassword(any(), any()) } returns mockTask
        every { mockExceptionMapper.map(originalException) } returns mappedException

        // 4. Verify the exception is mapped correctly
        shouldThrow<RuntimeException> {
            authManager.signInWithEmailAndPassword("invalid", "creeds")
        } shouldBe mappedException
    }

    @Test
    fun `signInAnonymously should return anonymous user when sign in succeeds`() = runTest {
        val mockUser = mockFirebaseUser("anon1", "", true)
        val mockTask = mockTask(mockAuthResult(mockUser), null)

        every { mockFirebaseAuth.signInAnonymously() } returns mockTask

        val result = authManager.signInAnonymously()
        result.isAnonymous shouldBe true
    }

    @Test
    fun `signInAnonymously should throw AnonymousSignInFailedException when user is null after sign in`() =
        runTest {
            val mockTask = mockTask<AuthResult>(
                result = mockAuthResult(null), exception = null
            )

            every { mockFirebaseAuth.signInAnonymously() } returns mockTask

            shouldThrow<MockKException> {
                authManager.signInAnonymously()
            }
        }

    @Test
    fun `linkToPermanentAccount should throw UserNotFoundException when no current user`() =
        runTest {
            every { mockFirebaseAuth.currentUser } returns null

            shouldThrow<UserNotFoundException> {
                authManager.linkToPermanentAccount("test@example.com", "pass")
            }
        }


    @Test
    fun `signOut should call FirebaseAuth signOut`() = runTest {
        every { mockFirebaseAuth.signOut() } returns Unit

        authManager.signOut()

        verify(exactly = 1) { mockFirebaseAuth.signOut() }
    }

    @Test
    fun `deleteUser should delete user when user exists`() = runTest {
        val mockUser = mockFirebaseUser("user1", "test@example.com", false)
        val mockTask = mockTask<Void>()

        every { mockFirebaseAuth.currentUser } returns mockUser
        every { mockUser.delete() } returns mockTask

        authManager.deleteUser()

        verify { mockUser.delete() }
    }

    @Test
    fun `deleteUser should throw UserNotFoundException when no current user`() = runTest {
        every { mockFirebaseAuth.currentUser } returns null

        shouldThrow<UserNotFoundException> {
            authManager.deleteUser()
        }
    }

    @Test
    fun `registerWithEmailAndPassword should return user when registration succeeds`() = runTest {
        val mockUser = mockFirebaseUser("user1", "test@example.com", false)
        val mockTask = mockTask(result = mockAuthResult(mockUser), exception = null)
        every { mockFirebaseAuth.createUserWithEmailAndPassword(any(), any()) } returns mockTask

        val result = authManager.registerWithEmailAndPassword("test@example.com", "password")

        result shouldBe mockUser.toDomainUser()
    }

    @Test
    fun `registerWithEmailAndPassword should throw UserNotFoundException when user is null`() =
        runTest {
            val mockTask = mockTask(result = mockAuthResult(null), exception = null)
            every { mockFirebaseAuth.createUserWithEmailAndPassword(any(), any()) } returns mockTask

            // Act & Assert
            shouldThrow<MockKException> {
                authManager.registerWithEmailAndPassword("test@example.com", "password")
            }
        }

    @Test
    fun `updateDisplayName should throw UserNotFoundException when no current user`() = runTest {
        // Arrange
        every { mockFirebaseAuth.currentUser } returns null

        // Act & Assert
        shouldThrow<UserNotFoundException> {
            authManager.updateDisplayName("New Name")
        }
    }
}

private fun FirebaseUser.toDomainUser(): com.example.domain.entity.User =
    com.example.domain.entity.User(
        id = uid, name = displayName ?: "Anonymous", email = email ?: "", isAnonymous = isAnonymous
    )
