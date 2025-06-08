package com.chatapp.pingnest.data.models

data class ChatMessage(
    val id: String,
    val chatId: String,
    val senderId: String,
    val recipientId: String,
    val content: String,
    val timestamp: String
)
