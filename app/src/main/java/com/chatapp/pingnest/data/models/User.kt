package com.chatapp.pingnest.data.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val nickName: String,
    val fullName: String,
    val status: Status? = null
)