package com.chatapp.pingnest.ui.screens.settings

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatapp.pingnest.data.models.AppSettings
import com.chatapp.pingnest.data.models.AppTheme
import com.chatapp.pingnest.data.models.ChatThemeType
import com.chatapp.pingnest.data.models.ChatWallpaperType
import com.chatapp.pingnest.data.models.User
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val appSettings: DataStore<AppSettings>
): ViewModel() {

    fun logout(){
        viewModelScope.launch {
          appSettings.updateData {
              it.copy(
                  userData = User(nickName = "", fullName = "")
              )
          }
        }
    }
    fun updateTheme(theme: AppTheme){
        viewModelScope.launch {
            appSettings.updateData {
                it.copy(
                    theme = theme
                )
            }
        }
    }
    val themeFlow = appSettings.data.map { it.theme }

    fun updateChatTheme(chatTheme: ChatThemeType){
        viewModelScope.launch {
            appSettings.updateData {
                it.copy(
                    chatThemeType = chatTheme
                )
            }
        }
    }
    val chatThemeFlow = appSettings.data.map { it.chatThemeType }

    fun updateWallpaper(wallpaper: ChatWallpaperType) {
        viewModelScope.launch {
            appSettings.updateData {
                it.copy(
                    chatWallpaperType =  wallpaper
                )
            }
        }
    }

    val wallpaperFlow = appSettings.data.map { it.chatWallpaperType }

}