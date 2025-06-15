package com.chatapp.pingnest.data.local

import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow

val FULL_NAME = stringPreferencesKey("first_name")
val NICK_NAME = stringPreferencesKey("nick_name")

interface UserData {

    fun getFullName(): Flow<String>
    fun getNickName(): Flow<String>
    suspend fun saveFullName(firstName: String)
    suspend fun saveNickName(nickName: String)
    suspend fun clearUser()

}