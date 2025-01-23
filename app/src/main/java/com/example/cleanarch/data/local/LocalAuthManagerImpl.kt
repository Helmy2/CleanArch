package com.example.cleanarch.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.cleanarch.domain.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalAuthManagerImpl(
    private val dataStore: DataStore<Preferences>
) : LocalAuthManager {
    companion object {
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_IS_ANONYMOUS = booleanPreferencesKey("user_is_anonymous")
    }

    override suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = user.id
            preferences[USER_NAME] = user.name
            preferences[USER_EMAIL] = user.email
            preferences[USER_IS_ANONYMOUS] = user.isAnonymous
        }
    }

    override fun getCurrentUser(): Flow<User?> {
        return dataStore.data.map { preferences ->
            preferences[USER_ID]?.let { id ->
                User(
                    id = id,
                    name = preferences[USER_NAME] ?: "",
                    email = preferences[USER_EMAIL] ?: "",
                    isAnonymous = preferences[USER_IS_ANONYMOUS] == true
                )
            }
        }
    }

    override suspend fun clearUser() {
        dataStore.edit { it.clear() }
    }
}