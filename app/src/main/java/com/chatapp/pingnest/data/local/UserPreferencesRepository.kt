package com.chatapp.pingnest.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map


class UserPreferencesRepository(
    private val datastore: DataStore<Preferences>
): UserData {



    override suspend fun clearUser() {
        datastore.edit { preferences ->
            preferences.clear()
        }
    }

    override fun getFullName(): Flow<String> {
        return datastore.data.catch { emit(emptyPreferences())   }.map {
            it[FULL_NAME] ?: ""
        }
    }

    override fun getNickName(): Flow<String> {
       return datastore.data.catch { emit(emptyPreferences())   }.map {
           it[NICK_NAME] ?: ""
       }
    }

    override suspend fun saveFullName(firstName: String) {
        datastore.edit {
            it[FULL_NAME] = firstName
        }
    }

    override suspend fun saveNickName(nickName: String) {
       datastore.edit {
           it[NICK_NAME] = nickName
       }
    }


}