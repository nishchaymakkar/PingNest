package com.chatapp.pingnest.data.models

data class ChatNotification(
    val id: String,
    val senderId: String,
    val recipientId: String,
    val content: String
)