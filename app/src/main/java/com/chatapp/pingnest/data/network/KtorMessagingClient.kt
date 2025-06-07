package com.chatapp.pingnest.data.network

import android.util.Log
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.data.models.dto.ChatMessageDto
import com.chatapp.pingnest.data.models.dto.UserDto

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.*
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.serialization.json.Json
import java.net.ConnectException
import java.net.SocketTimeoutException

val localhost = "192.168.1.254"
class KtorStompMessagingClient(
    private val client: HttpClient
): RealtimeMessagingClient{

    private var session: WebSocketSession? = null
    private val incomingMessages = MutableSharedFlow<String>()

    override suspend fun getUsers(): List<UserDto> {
        return try {
            client.get("http://$localhost:8088/users").body()
        }catch (e: HttpRequestTimeoutException) {
            Log.e("Network", "Timeout occurred: ${e.message}")
            emptyList() // or handle accordingly
        } catch (e: ConnectException) {
            Log.e("Network", "Server unreachable: ${e.message}")
            emptyList()
        } catch (e: SocketTimeoutException) {
            Log.e("Network", "Socket timeout: ${e.message}")
            emptyList()
        } catch (e: Exception) {
            Log.e("Network", "Unexpected error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getMessages(
        senderId: String,
        recipientId: String
    ): List<ChatMessageDto> {
        return client.get("http://$localhost:8088/messages/$senderId/$recipientId").body()
    }

    override suspend fun connect() {
        session = client.webSocketSession {
            url("ws://$localhost:8088/ws")
        }
        session?.let {
            if (it.isActive){
                println("Session Active")
            } else{
                println("Session Closed")
            }
        }
        session?.send(Frame.Text("CONNECT\naccept-version:1.2\n\n\u0000"))

        CoroutineScope(Dispatchers.IO).launch {
            session?.incoming
                ?.consumeAsFlow()
                ?.filterIsInstance<Frame.Text>()
                ?.collect {
                    incomingMessages.emit(it.readText())
                }
        }
    }

    override suspend fun subscribe(destination: String) {
        session?.send(Frame.Text("SUBSCRIBE\ndestination:$destination\nid:${destination.hashCode()}\n\n\u0000"))
    }

    override suspend fun addUser(
        destination: String,
        user: UserDto
    ) {
        val json = Json.encodeToString(user)
        val frame = "SEND\ndestination:$destination\ncontent-type:application/json\n\n$json\u0000"
        session?.send(Frame.Text(frame))
    }

    override suspend fun sendMessage(message: ChatMessageDto) {
        val frame = "SEND\ndestination:/queue/messages\ncontent-type:application/json\n\n$message\u0000"
        session?.send(Frame.Text(frame))
    }

    override fun observeMessages(): Flow<String>  = incomingMessages


    override suspend fun disconnect() {
        session?.send(Frame.Text("DISCONNECT\n\n\u0000"))
        session?.close()
        client.close()

    }


}
