package com.example.cleanarch.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.cleanarch.domain.entity.User
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class LocalAuthManagerTest {

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private lateinit var testDataStore: DataStore<Preferences>
    private lateinit var localUserManager: LocalAuthManager

    @Before
    fun setUp() {
        testDataStore =
            PreferenceDataStoreFactory.create(produceFile = { tmpFolder.newFile("user.preferences_pb") })
        localUserManager = LocalAuthManagerImpl(testDataStore)
    }

    @Test
    fun `saveUser should save user data to DataStore`() = runTest {
        // Given: A user object to save
        val user = User("123", "Test User", "test@example.com", false)

        // When: The user is saved to DataStore
        localUserManager.saveUser(user)

        // Then: The saved user should match the original user
        val savedUser = localUserManager.getCurrentUser().first()
        savedUser.shouldBe(user)
    }

    @Test
    fun `saveUser should overwrite existing user data`() = runTest {
        // Given: An initial user is saved
        val initialUser = User("123", "Test User", "test@example.com", false)
        localUserManager.saveUser(initialUser)

        // When: A new user is saved with the same ID
        val newUser = User("123", "New User", "new@example.com", true)
        localUserManager.saveUser(newUser)

        // Then: The saved user should be the new user
        val savedUser = localUserManager.getCurrentUser().first()
        savedUser.shouldBe(newUser)
    }

    @Test
    fun `saveUser should handle user with empty fields`() = runTest {
        // Given: A user with empty fields
        val user = User("", "", "", false)

        // When: The user is saved
        localUserManager.saveUser(user)

        // Then: The saved user should match the original user
        val savedUser = localUserManager.getCurrentUser().first()
        savedUser.shouldBe(user)
    }

    @Test
    fun `saveUser should handle large user data`() = runTest {
        // Given: A user with large data (e.g., long strings)
        val largeUser = User(
            id = "123", name = "A".repeat(10000), // Large name
            email = "B".repeat(10000), // Large email
            isAnonymous = false
        )

        // When: The large user is saved
        localUserManager.saveUser(largeUser)

        // Then: The saved user should match the original user
        val savedUser = localUserManager.getCurrentUser().first()
        savedUser.shouldBe(largeUser)
    }

    @Test
    fun `getCurrentUser should emit null when no user is saved`() = runTest {
        // When: No user is saved
        val result = localUserManager.getCurrentUser().first()

        // Then: The result should be null
        result.shouldBeNull()
    }

    @Test
    fun `clearUser should remove user data from DataStore`() = runTest {
        // Given: A user is saved
        val user = User("123", "Test User", "test@example.com", false)
        localUserManager.saveUser(user)

        // When: The user data is cleared
        localUserManager.clearUser()

        // Then: The result should be null
        val result = localUserManager.getCurrentUser().first()
        result.shouldBeNull()
    }

    @Test
    fun `clearUser should do nothing when no user is saved`() = runTest {
        // When: clearUser is called with no user saved
        localUserManager.clearUser()

        // Then: The result should still be null
        val result = localUserManager.getCurrentUser().first()
        result.shouldBeNull()
    }

}