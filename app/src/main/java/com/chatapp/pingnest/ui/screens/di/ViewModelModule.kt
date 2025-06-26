package com.chatapp.pingnest.ui.screens.di

import com.chatapp.pingnest.ui.PingNestViewModel
import com.chatapp.pingnest.ui.photopicker.PhotoPickerViewModel
import com.chatapp.pingnest.ui.screens.chatroom.ChatRoomViewModel
import com.chatapp.pingnest.ui.screens.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::PingNestViewModel)
    viewModelOf(::ChatRoomViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::PhotoPickerViewModel)
}
