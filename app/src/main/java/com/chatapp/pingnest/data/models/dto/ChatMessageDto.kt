package com.chatapp.pingnest.data.models.dto

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class ChatMessageDto(
    val id: String? = null,
    val chatId: String? = null,
    val senderId: String,
    val recipientId: String,
    val content: String,
    val timeStamp: String? = null
)
