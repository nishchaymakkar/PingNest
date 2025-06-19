package com.chatapp.pingnest.data.network

import android.util.Log
import com.chatapp.pingnest.data.models.dto.ChatMessageDto
import com.chatapp.pingnest.data.models.dto.UserDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*


const val localhost = "192.168.1.254"



class PingNestApiServiceImpl(
    private val client: HttpClient
) : PingNestApiService {

    override suspend fun getUsers(): List<UserDto> {
        return try {
            client.get("http://$localhost:8088/users").body()
        } catch (e: Exception) {
            Log.e("Network", "Error fetching users: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getMessages(senderId: String, recipientId: String): List<ChatMessageDto> {
        return try {
            client.get("http://$localhost:8088/messages/$senderId/$recipientId").body()
        } catch (e: Exception) {
            Log.e("Network", "Error fetching messages: ${e.message}")
            emptyList()
        }
    }


}
