package com.chatapp.pingnest.data.models

import java.util.Date

data class ChatMessage(
    val id: String,
    val chatId: String,
    val senderId: String,
    val recipientId: String,
    val content: String,
    val timestamp: Date
)
