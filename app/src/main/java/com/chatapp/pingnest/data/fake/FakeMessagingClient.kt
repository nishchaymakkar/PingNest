package com.chatapp.pingnest.data.fake

import com.chatapp.pingnest.data.models.ChatNotification
import com.chatapp.pingnest.data.models.dto.ChatMessageDto
import com.chatapp.pingnest.data.models.dto.UserDto
import com.chatapp.pingnest.data.network.RealtimeMessagingClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeMessagingClient : RealtimeMessagingClient {
    private val messagesFlow = MutableSharedFlow<ChatNotification>(replay = 0)

    override suspend fun connect() {
        // No-op
    }

    override suspend fun subscribe(destination: String) {
        // No-op
    }

    override suspend fun addUser(destination: String, user: UserDto) {
        // Simulate user presence
    }

    override suspend fun sendMessage(message: ChatMessageDto) {
        val notification = ChatNotification(
            id = message.id ?: "local-${System.currentTimeMillis()}",
            senderId = message.senderId,
            recipientId = message.recipientId,
            content = message.content
        )
        messagesFlow.emit(notification)
    }

    override fun observeMessages(): Flow<ChatNotification> = messagesFlow

    override suspend fun disconnect() {
        // No-op
    }
}
