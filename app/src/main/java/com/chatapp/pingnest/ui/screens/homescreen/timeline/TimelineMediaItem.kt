package com.chatapp.pingnest.ui.screens.homescreen.timeline

import android.net.Uri

data class TimelineMediaItem(
    val uri: String,
    val type: TimelineMediaType,
    val timeStamp: Long,
    val chatName: String,
    val chatIconUri: Uri?
)

enum class TimelineMediaType{
    PHOTO,
    VIDEO
}
