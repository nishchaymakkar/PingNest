package com.chatapp.pingnest.data.network

import android.os.Build
import android.util.Log
import com.chatapp.pingnest.data.NotificationHelper
import com.chatapp.pingnest.data.models.ChatNotification
import com.chatapp.pingnest.data.models.dto.ChatMessageDto
import com.chatapp.pingnest.data.models.dto.UserDto
import com.chatapp.pingnest.ui.mappers.toChatMessageFromNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import org.hildan.krossbow.stomp.frame.FrameBody
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
class StompMessagingClient (
    private val notificationHelper: NotificationHelper
): RealtimeMessagingClient{

    private val webSocketClient = OkHttpWebSocketClient()
    private val stompClient = StompClient(webSocketClient)

    private var stompSession: StompSession? = null
    private val incomingMessages = MutableSharedFlow<ChatNotification>()

    override suspend fun connect() {
        try {
            stompSession = stompClient.connect("ws://$localhost:8088/ws")
            Log.d("Stomp", "Connected")
        } catch (e: Exception) {
            Log.e("Stomp", "Connection failed", e)
        }
    }

    override suspend fun subscribe(destination: String) {
        Log.d("StompMessagingClient","Subscribing to $destination")
        if (stompSession == null) {
            Log.e("Stomp", "Not connected")
            return
        }
        val session = stompSession
        val messageFlow = session?.subscribe(StompSubscribeHeaders(destination))

        CoroutineScope(Dispatchers.IO).launch {
            messageFlow?.collect { message ->
                try {
                    val notification = Json.decodeFromString<ChatNotification>(message.bodyAsText)
                    incomingMessages.emit(notification)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        notificationHelper.sendNotification(notification.content,notification.senderId)
                    }
                    Log.d("StompMessagingClient", "Received message: $notification")
                } catch (e: Exception) {
                    Log.e("Stomp", "Failed to parse message", e)
                }
            }
        }
    }


    override suspend fun addUser(destination: String, user: UserDto) {
        if (stompSession == null) {
            Log.e("Stomp", "Not connected")
            return
        }
        val session = stompSession

        val json = Json.encodeToString(user)
        session?.send(
            headers = StompSendHeaders(destination),
            body = FrameBody.Text(json)
        )
    }


    override suspend fun sendMessage(message: ChatMessageDto) {
        if (stompSession == null) {
            Log.e("Stomp", "Not connected")
            return
        }
        val session = stompSession
        val json = Json.encodeToString(message)
        session?.send(
            headers = StompSendHeaders("/app/chat"),
            body = FrameBody.Text(json)
        )
    }

    override fun observeMessages(): Flow<ChatNotification> = incomingMessages

    override suspend fun disconnect() {
        stompSession?.disconnect()
        Log.d("Stomp", "Disconnected")
    }



}