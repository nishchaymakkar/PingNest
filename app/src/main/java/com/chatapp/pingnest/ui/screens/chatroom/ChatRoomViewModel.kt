package com.chatapp.pingnest.ui.screens.chatroom

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import com.chatapp.pingnest.data.models.AppSettings
import kotlinx.coroutines.flow.map

class ChatRoomViewModel(
    private val appSettings: DataStore<AppSettings>
) : ViewModel() {
    val chatThemeFlow = appSettings.data.map { it.chatThemeType }
    val wallpaperFlow = appSettings.data.map { it.chatWallpaperType }

}