package com.example.cleanarch.data.repository

import com.example.cleanarch.data.local.LocalAuthManager
import com.example.cleanarch.data.remote.RemoteAuthManager
import com.example.cleanarch.domain.entity.Resource
import com.example.cleanarch.domain.entity.User
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {
    private val mockRemoteAuthManager: RemoteAuthManager = mockk()
    private val mockLocalAuthManager: LocalAuthManager = mockk()
    private lateinit var authRepository: AuthRepositoryImpl


    @Before
    fun setup() {
        authRepository = AuthRepositoryImpl(
            mockRemoteAuthManager,
            mockLocalAuthManager,
            Dispatchers.IO
        )
    }

    @Test
    fun `getCurrentUser should emit local user`() = runTest {
        // Arrange
        val mockUser = User("123", "Test User", "test@example.com", false)
        coEvery { mockLocalAuthManager.getCurrentUser() } returns flowOf(mockUser)
        coEvery { mockRemoteAuthManager.getAuthState() } returns flowOf(null)

        // Act
        val result = authRepository.getCurrentUser().take(2).toList()

        // Assert
        result[0] shouldBe Resource.Loading
        result[1] shouldBe Resource.Success(mockUser)
    }

    @Test
    fun `getCurrentUser should emit remote user and update local storage`() = runTest {
        // Arrange
        val mockUser = User("123", "Test User", "test@example.com", false)
        coEvery { mockLocalAuthManager.getCurrentUser() } returns flowOf(null)
        coEvery { mockRemoteAuthManager.getAuthState() } returns flowOf(mockUser)
        coEvery { mockLocalAuthManager.saveUser(mockUser) } returns Unit

        // Act
        val result = authRepository.getCurrentUser().take(3).toList()

        // Assert
        result[0] shouldBe Resource.Loading
        result[1] shouldBe Resource.Success(null)
        result[2] shouldBe Resource.Success(mockUser)
        coVerify(exactly = 1) { mockLocalAuthManager.saveUser(mockUser) }
    }

    @Test
    fun `getCurrentUser should clear local storage on remote sign-out`() = runTest {
        // Arrange
        coEvery { mockLocalAuthManager.getCurrentUser() } returns flowOf(null)
        coEvery { mockRemoteAuthManager.getAuthState() } returns flowOf(null)
        coEvery { mockLocalAuthManager.clearUser() } returns Unit

        // Act
        val result = authRepository.getCurrentUser().take(2).toList()

        // Assert
        result[0] shouldBe Resource.Loading
        result[1] shouldBe Resource.Success(null)
        coVerify(exactly = 1) { mockLocalAuthManager.clearUser() }
    }

    @Test
    fun `signInWithEmailAndPassword should return success and save user`() = runTest {
        // Arrange
        val mockUser = User("123", "Test User", "test@example.com", false)
        coEvery { mockRemoteAuthManager.signInWithEmailAndPassword(any(), any()) } returns mockUser
        coEvery { mockLocalAuthManager.saveUser(mockUser) } returns Unit

        // Act
        val result = authRepository.signInWithEmailAndPassword("test@example.com", "password")

        // Assert
        result.isSuccess shouldBe true
        coVerify(exactly = 1) { mockLocalAuthManager.saveUser(mockUser) }
    }

    @Test
    fun `signInWithEmailAndPassword should return failure on error`() = runTest {
        // Arrange
        val exception = RuntimeException("Sign-in failed")
        coEvery { mockRemoteAuthManager.signInWithEmailAndPassword(any(), any()) } throws exception

        // Act
        val result = authRepository.signInWithEmailAndPassword("invalid", "creds")

        // Assert
        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe exception
        coVerify(exactly = 0) { mockLocalAuthManager.saveUser(any()) }
    }

    @Test
    fun `registerWithEmailAndPassword should return success and save user`() = runTest {
        // Arrange
        val mockUser = User("123", "Test User", "test@example.com", false)
        coEvery {
            mockRemoteAuthManager.registerWithEmailAndPassword(
                any(),
                any()
            )
        } returns mockUser
        coEvery { mockLocalAuthManager.saveUser(mockUser) } returns Unit

        // Act
        val result = authRepository.registerWithEmailAndPassword("test@example.com", "password")

        // Assert
        result.isSuccess shouldBe true
        coVerify(exactly = 1) { mockLocalAuthManager.saveUser(mockUser) }
    }

    @Test
    fun `registerWithEmailAndPassword should return failure on error`() = runTest {
        // Arrange
        val exception = RuntimeException("Registration failed")
        coEvery {
            mockRemoteAuthManager.registerWithEmailAndPassword(
                any(),
                any()
            )
        } throws exception

        // Act
        val result = authRepository.registerWithEmailAndPassword("invalid", "creds")

        // Assert
        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe exception
        coVerify(exactly = 0) { mockLocalAuthManager.saveUser(any()) }
    }

    @Test
    fun `signInAnonymously should return success and save user`() = runTest {
        // Arrange
        val mockUser = User("123", "Anonymous", "", true)
        coEvery { mockRemoteAuthManager.signInAnonymously() } returns mockUser
        coEvery { mockLocalAuthManager.saveUser(mockUser) } returns Unit

        // Act
        val result = authRepository.signInAnonymously()

        // Assert
        result.isSuccess shouldBe true
        coVerify(exactly = 1) { mockLocalAuthManager.saveUser(mockUser) }
    }

    @Test
    fun `signInAnonymously should return failure on error`() = runTest {
        // Arrange
        val exception = RuntimeException("Anonymous sign-in failed")
        coEvery { mockRemoteAuthManager.signInAnonymously() } throws exception

        // Act
        val result = authRepository.signInAnonymously()

        // Assert
        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe exception
        coVerify(exactly = 0) { mockLocalAuthManager.saveUser(any()) }
    }

    @Test
    fun `convertToPermanentAccount should return success and save user`() = runTest {
        // Arrange
        val mockUser = User("123", "Test User", "test@example.com", false)
        coEvery { mockRemoteAuthManager.linkToPermanentAccount(any(), any()) } returns mockUser
        coEvery { mockLocalAuthManager.saveUser(mockUser) } returns Unit

        // Act
        val result = authRepository.convertToPermanentAccount("test@example.com", "password")

        // Assert
        result.isSuccess shouldBe true
        coVerify(exactly = 1) { mockLocalAuthManager.saveUser(mockUser) }
    }


    @Test
    fun `convertToPermanentAccount should return failure on error`() = runTest {
        // Arrange
        val exception = RuntimeException("Conversion failed")
        coEvery { mockRemoteAuthManager.linkToPermanentAccount(any(), any()) } throws exception

        // Act
        val result = authRepository.convertToPermanentAccount("invalid", "creds")

        // Assert
        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe exception
        coVerify(exactly = 0) { mockLocalAuthManager.saveUser(any()) }
    }

    @Test
    fun `updateDisplayName should return success and save user`() = runTest {
        // Arrange
        val mockUser = User("123", "New Name", "test@example.com", false)
        coEvery { mockRemoteAuthManager.updateDisplayName(any()) } returns mockUser
        coEvery { mockLocalAuthManager.saveUser(mockUser) } returns Unit

        // Act
        val result = authRepository.updateDisplayName("New Name")

        // Assert
        result.isSuccess shouldBe true
        coVerify(exactly = 1) { mockLocalAuthManager.saveUser(mockUser) }
    }

    @Test
    fun `updateDisplayName should return failure on error`() = runTest {
        // Arrange
        val exception = RuntimeException("Update failed")
        coEvery { mockRemoteAuthManager.updateDisplayName(any()) } throws exception

        // Act
        val result = authRepository.updateDisplayName("Invalid Name")

        // Assert
        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe exception
        coVerify(exactly = 0) { mockLocalAuthManager.saveUser(any()) }
    }

    @Test
    fun `deleteUser should return success and clear local storage`() = runTest {
        // Arrange
        coEvery { mockRemoteAuthManager.deleteUser() } returns Unit
        coEvery { mockLocalAuthManager.clearUser() } returns Unit

        // Act
        val result = authRepository.deleteUser()

        // Assert
        result.isSuccess shouldBe true
        coVerify(exactly = 1) { mockLocalAuthManager.clearUser() }
    }


    @Test
    fun `deleteUser should return failure on error`() = runTest {
        // Arrange
        val exception = RuntimeException("Deletion failed")
        coEvery { mockRemoteAuthManager.deleteUser() } throws exception

        // Act
        val result = authRepository.deleteUser()

        // Assert
        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe exception
        coVerify(exactly = 0) { mockLocalAuthManager.clearUser() }
    }

    @Test
    fun `signOut should return success and clear local storage`() = runTest {
        // Arrange
        coEvery { mockRemoteAuthManager.signOut() } returns Unit
        coEvery { mockLocalAuthManager.clearUser() } returns Unit

        // Act
        val result = authRepository.signOut()

        // Assert
        result.isSuccess shouldBe true
        coVerify(exactly = 1) { mockLocalAuthManager.clearUser() }
    }

    @Test
    fun `signOut should return failure on error`() = runTest {
        // Arrange
        val exception = RuntimeException("Sign-out failed")
        coEvery { mockRemoteAuthManager.signOut() } throws exception

        // Act
        val result = authRepository.signOut()

        // Assert
        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe exception
        coVerify(exactly = 0) { mockLocalAuthManager.clearUser() }
    }

}