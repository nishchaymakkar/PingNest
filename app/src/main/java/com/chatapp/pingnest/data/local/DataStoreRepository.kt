package com.chatapp.pingnest.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class DataStoreRepository(
    private val datastore: DataStore<Preferences>
) {
    private companion object {
        val FULLNAME = stringPreferencesKey("full_name")
        val NICKNAME = stringPreferencesKey("nick_name")
    }

    val fullName: Flow<String?> = datastore.data.map { preferences ->
        preferences[FULLNAME]
    }
    val nickname: Flow<String?> = datastore.data.map { preferences ->
        preferences[NICKNAME]
    }


    suspend fun saveUser(fullName: String,nickname: String) {
        datastore.edit { preferences ->
            preferences[FULLNAME] = fullName
            preferences[NICKNAME] = nickname
        }
    }

    suspend fun clearUser() {
        datastore.edit { preferences ->
            preferences.clear()
        }
    }

    /**
     * Checks if a user is currently logged in.
     * @return A Flow emitting true if both full name and nickname are present, false otherwise.
     */
    fun currentUser(): Flow<Boolean> = datastore.data.map { preferences ->
        val fullName = preferences[FULLNAME]
        val nickname = preferences[NICKNAME]
        !fullName.isNullOrEmpty() && !nickname.isNullOrEmpty()
    }

}