package com.chatapp.pingnest.data.network


import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.data.models.dto.ChatMessageDto
import com.chatapp.pingnest.data.models.dto.UserDto
import kotlinx.coroutines.flow.Flow

interface RealtimeMessagingClient {
    suspend fun getUsers(): List<UserDto>
    suspend fun getMessages(senderId: String, recipientId: String): List<ChatMessageDto>

    suspend fun connect()
    suspend fun subscribe(destination: String)
    suspend fun addUser(destination:String, user: UserDto)
    suspend fun sendMessage(message: ChatMessageDto)
    fun observeMessages(): Flow<String>
    suspend fun disconnect()
}
