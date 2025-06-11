package com.chatapp.pingnest.data.models

data class User(
    val nickName: String,
    val fullName: String,
    val status: Status? = null
)