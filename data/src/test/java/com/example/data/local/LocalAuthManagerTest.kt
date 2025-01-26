package com.example.data.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.domain.entity.User
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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
        val user = User("123", "Test User", "test@example.com", false)

        localUserManager.saveUser(user)

        val savedUser = localUserManager.getCurrentUser().first()
        savedUser.shouldBe(user)
    }

    @Test
    fun `saveUser should overwrite existing user data`() = runTest {
        val initialUser =
            User("123", "Test User", "test@example.com", false)
        localUserManager.saveUser(initialUser)

        val newUser = User("123", "New User", "new@example.com", true)
        localUserManager.saveUser(newUser)

        val savedUser = localUserManager.getCurrentUser().first()
        savedUser.shouldBe(newUser)
    }

    @Test
    fun `saveUser should handle user with empty fields`() = runTest {
        val user = User("", "", "", false)

        localUserManager.saveUser(user)

        val savedUser = localUserManager.getCurrentUser().first()
        savedUser.shouldBe(user)
    }

    @Test
    fun `saveUser should handle large user data`() = runTest {
        val largeUser = User(
            id = "123", name = "A".repeat(10000), // Large name
            email = "B".repeat(10000), // Large email
            isAnonymous = false
        )

        localUserManager.saveUser(largeUser)

        val savedUser = localUserManager.getCurrentUser().first()
        savedUser.shouldBe(largeUser)
    }

    @Test
    fun `saveUser should handle DataStore write errors`() = runTest {
        // Mock DataStore to throw an exception
        val mockDataStore = mockk<DataStore<Preferences>> {
            coEvery { updateData(any()) } throws IOException("Write failed")
        }
        val errorLocalUserManager = LocalAuthManagerImpl(mockDataStore)

        val user = User("123", "Test User", "test@example.com", false)

        shouldThrow<IOException> {
            errorLocalUserManager.saveUser(user)
        }
    }

    @Test
    fun `getCurrentUser should emit null when no user is saved`() = runTest {
        val result = localUserManager.getCurrentUser().first()

        result.shouldBeNull()
    }

    @Test
    fun `getCurrentUser should handle corrupted DataStore`() = runTest {
        // Simulate a corrupted DataStore file
        val corruptedFile = tmpFolder.newFile("corrupted.preferences_pb")
        corruptedFile.writeText("invalid data")

        val corruptedDataStore = PreferenceDataStoreFactory.create(produceFile = { corruptedFile })
        val corruptedLocalUserManager =
            LocalAuthManagerImpl(corruptedDataStore)

        // Verify that the corrupted DataStore is handled gracefully
        shouldThrow<CorruptionException> {
            corruptedLocalUserManager.getCurrentUser().first()
        }
    }

    @Test
    fun `concurrent saveUser and getCurrentUser should not cause data corruption`() = runTest {
        val user1 = User("123", "User 1", "user1@example.com", false)
        val user2 = User("456", "User 2", "user2@example.com", true)

        // Perform concurrent operations
        val saveJob1 = launch { localUserManager.saveUser(user1) }
        val saveJob2 = launch { localUserManager.saveUser(user2) }
        val getJob = launch { localUserManager.getCurrentUser().first() }

        // Wait for all operations to complete
        saveJob1.join()
        saveJob2.join()
        getJob.join()

        // Verify the final state
        val savedUser = localUserManager.getCurrentUser().first()
        savedUser shouldBe user2 // Last saved user should be the final state
    }

    @Test
    fun `clearUser should remove user data from DataStore`() = runTest {
        val user = User("123", "Test User", "test@example.com", false)
        localUserManager.saveUser(user)

        localUserManager.clearUser()

        val result = localUserManager.getCurrentUser().first()
        result.shouldBeNull()
    }

    @Test
    fun `clearUser should do nothing when no user is saved`() = runTest {
        localUserManager.clearUser()

        val result = localUserManager.getCurrentUser().first()
        result.shouldBeNull()
    }

    @Test
    fun `concurrent clearUser and saveUser should not cause data corruption`() = runTest {
        val user = User("123", "Test User", "test@example.com", false)

        // Perform concurrent operations
        val saveJob = launch { localUserManager.saveUser(user) }
        val clearJob = launch { localUserManager.clearUser() }
        val getJob = launch { localUserManager.getCurrentUser().first() }

        // Wait for all operations to complete
        saveJob.join()
        clearJob.join()
        getJob.join()

        // Verify the final state
        val savedUser = localUserManager.getCurrentUser().first()
        savedUser.shouldBeNull() // clearUser should take precedence
    }
}