package com.chatapp.pingnest.data.models.dto

import com.chatapp.pingnest.data.models.Status
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val nickName: String,
    val fullName: String,
    val status: Status
)
