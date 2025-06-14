package com.chatapp.pingnest.data.network


import com.chatapp.pingnest.data.models.ChatNotification
import com.chatapp.pingnest.data.models.dto.ChatMessageDto
import com.chatapp.pingnest.data.models.dto.UserDto
import kotlinx.coroutines.flow.Flow

interface RealtimeMessagingClient {
    suspend fun connect()
    suspend fun subscribe(destination: String)
    suspend fun addUser(destination:String, user: UserDto)
    suspend fun sendMessage(message: ChatMessageDto)
    fun observeMessages(): Flow<ChatNotification>
    suspend fun disconnect()
}
