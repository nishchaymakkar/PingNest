package com.chatapp.pingnest.data.network

import android.app.Notification
import android.util.Log
import androidx.core.app.NotificationCompat
import com.chatapp.pingnest.data.models.ChatNotification
import com.chatapp.pingnest.data.models.dto.ChatMessageDto
import com.chatapp.pingnest.data.models.dto.UserDto
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.json.Json
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

const val localhost = "192.168.1.254"



class KtorStompMessagingClient(
    private val client: HttpClient
) : PingNestApiService {

    private var subscription: Disposable? = null
    private lateinit var stompClient: StompClient
    private val incomingMessages = MutableSharedFlow<ChatNotification>()
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
