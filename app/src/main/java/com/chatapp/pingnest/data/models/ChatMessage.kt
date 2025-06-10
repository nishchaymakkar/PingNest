package com.chatapp.pingnest.data.models

data class ChatMessage(
    val id: String? = null,
    val chatId: String? = null,
    val senderId: String,
    val recipientId: String,
    val content: String,
    val timestamp: String
)
