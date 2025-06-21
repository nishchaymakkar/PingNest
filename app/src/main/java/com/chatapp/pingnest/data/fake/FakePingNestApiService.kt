package com.chatapp.pingnest.data.fake

import com.chatapp.pingnest.data.models.Status
import com.chatapp.pingnest.data.models.dto.ChatMessageDto
import com.chatapp.pingnest.data.models.dto.UserDto
import com.chatapp.pingnest.data.network.PingNestApiService

class FakePingNestApiService : PingNestApiService {
    private val dummyUsers = listOf(
        UserDto("john", "John Doe", Status.ONLINE),
        UserDto("jane", "Jane Smith", Status.OFFLINE)
    )

    private val dummyMessages = listOf(
        ChatMessageDto("1", "chat1", "john", "jane", "Hey Jane!", "2025-06-09 10:00:00"),
        ChatMessageDto("2", "chat1", "jane", "john", "Hi John!", "2025-06-09 10:01:00")
    )

    override suspend fun getUsers(): List<UserDto> = dummyUsers

    override suspend fun getMessages(senderId: String, recipientId: String): List<ChatMessageDto> {
        return dummyMessages.filter {
            (it.senderId == senderId && it.recipientId == recipientId) ||
            (it.senderId == recipientId && it.recipientId == senderId)
        }
    }
}
