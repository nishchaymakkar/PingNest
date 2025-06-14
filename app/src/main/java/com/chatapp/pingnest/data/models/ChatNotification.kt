package com.chatapp.pingnest.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatNotification(
    val id: String,
    val senderId: String,
    val recipientId: String,
    val content: String
)