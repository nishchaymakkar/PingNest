package com.chatapp.pingnest.ui.di

import android.annotation.SuppressLint
import com.chatapp.pingnest.ui.PingNestViewModel
import com.chatapp.pingnest.ui.camera.CameraViewModel
import com.chatapp.pingnest.ui.photopicker.PhotoPickerViewModel
import com.chatapp.pingnest.ui.screens.chatroom.ChatRoomViewModel
import com.chatapp.pingnest.ui.screens.homescreen.timeline.TimelineViewModel
import com.chatapp.pingnest.ui.screens.settings.SettingsViewModel
import com.chatapp.pingnest.ui.videoedit.VideoEditScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@SuppressLint("UnsafeOptInUsageError")
val viewModelModule = module {
    viewModelOf(::PingNestViewModel)
    viewModelOf(::ChatRoomViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::PhotoPickerViewModel)
    viewModelOf(::CameraViewModel)
    viewModelOf(::VideoEditScreenViewModel)
    viewModelOf(::TimelineViewModel)
}
