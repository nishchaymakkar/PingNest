package com.chatapp.pingnest.ui.mappers

import com.chatapp.pingnest.data.models.ChatMessage
import com.chatapp.pingnest.data.models.ChatNotification
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.data.models.dto.ChatMessageDto
import com.chatapp.pingnest.data.models.dto.UserDto

fun UserDto.toUser(): User = User(
    nickName = this.nickName,
    fullName = this.fullName,
    status = this.status
)

fun ChatMessageDto.toChatMessage(): ChatMessage = ChatMessage(
    senderId = this.senderId,
    recipientId = this.recipientId,
    content = this.content,
    id = this.id,
    timestamp = this.timeStamp ?: "",
    chatId = this.chatId
)

fun ChatMessage.toChatMessageDto(): ChatMessageDto = ChatMessageDto(
    senderId = this.senderId,
    recipientId = this.recipientId,
    content = this.content,
    id = this.id,
    timeStamp = this.timestamp,
    chatId = this.chatId
)

fun User.toUserDto(): UserDto = UserDto(
    nickName = this.nickName,
    fullName = this.fullName,
    status = this.status
)
fun ChatNotification.toChatMessageFromNotification() = ChatMessage(
    senderId = this.senderId,
    recipientId = this.recipientId,
    content = this.content,
    id = this.id,
    timestamp = "unkown"
)