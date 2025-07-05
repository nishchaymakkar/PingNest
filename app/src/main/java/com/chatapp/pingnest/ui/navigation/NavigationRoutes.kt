package com.chatapp.pingnest.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.chatapp.pingnest.data.models.User
import kotlinx.serialization.Serializable

@Serializable
data object HomeScreen : NavKey

@Serializable
data class ChatRoom(val user: User) : NavKey
@Serializable
data object PhotoPicker: NavKey
@Serializable
data class CameraScreen(val user: User) : NavKey
@Serializable
data object DemoScreen: NavKey
@Serializable
data class VideoEditor(val user: User, val uri: String) : NavKey
@Serializable
sealed class Settings : NavKey{
    @Serializable
    data object SettingsScreen : Settings()
    @Serializable
    data object AccountSettingsScreen: Settings()
    @Serializable
    data object PrivacySettingsScreen: Settings()
    @Serializable
    data object HelpSettingsScreen: Settings()
    @Serializable
    data object AppLanguageSettingsScreen: Settings()
    @Serializable
    data object ChatThemSettingsScreen: Settings()
    @Serializable
    data object ProfileSettingsScreen: Settings()
}



