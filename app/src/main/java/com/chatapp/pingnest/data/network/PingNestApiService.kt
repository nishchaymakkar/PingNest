package com.chatapp.pingnest.data.network

import com.chatapp.pingnest.data.models.dto.ChatMessageDto
import com.chatapp.pingnest.data.models.dto.UserDto

interface PingNestApiService {
    suspend fun getUsers(): List<UserDto>
    suspend fun getMessages(senderId: String, recipientId: String): List<ChatMessageDto>

}