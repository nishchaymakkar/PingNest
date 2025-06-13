package com.chatapp.pingnest.data.network

import android.util.Log
import com.chatapp.pingnest.data.models.dto.ChatMessageDto
import com.chatapp.pingnest.data.models.dto.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

const val localhost = "192.168.1.254"

/**

class KtorStompMessagingClient(
    private val client: HttpClient
): RealtimeMessagingClient{

    private var session: WebSocketSession? = null
    private lateinit var stompClient: StompClient
    private val incomingMessages = MutableSharedFlow<String>(extraBufferCapacity = 64)


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
        return try {
            client.get("http://$localhost:8088/messages/$senderId/$recipientId").body()
        }catch (e: HttpRequestTimeoutException) {
            Log.e("Network", "Timeout occurred while fetching messages: ${e.message}")
            emptyList() // or handle accordingly
        } catch (e: ConnectException) {
            Log.e("Network", "Server unreachable while fetching messages: ${e.message}")
            emptyList()
        } catch (e: SocketTimeoutException) {
            Log.e("Network", "Socket timeout while fetching messages: ${e.message}")
            emptyList()
        } catch (e: Exception) {
            Log.e("Network", "Unexpected error while fetching messages: ${e.message}")
            emptyList()
        }
    }

    override suspend fun connect() {

        withContext(Dispatchers.IO) {
            stompClient = Stomp.over(
                Stomp.ConnectionProvider.OKHTTP,
                "ws://$localhost:8088/ws"
            )

            stompClient.lifecycle()
                .subscribe{ lifecycleEvent ->
                    when (lifecycleEvent.type) {
                        LifecycleEvent.Type.OPENED -> Log.d("Stomp", "Connection opened")
                        LifecycleEvent.Type.ERROR -> Log.e("Stomp", "Connection error", lifecycleEvent.exception)
                        LifecycleEvent.Type.CLOSED -> Log.d("Stomp", "Connection closed")
                        else -> Unit
                    }
                }

            stompClient.connect()
        }



//        session = client.webSocketSession {
//            url("ws://$localhost:8088/ws")
//        }
//        session?.let {
//            if (it.isActive){
//                println("Session Active")
//            } else{
//                println("Session Closed")
//            }
//        }
//        session?.send(Frame.Text("CONNECT\naccept-version:1.2\n\n\u0000"))
//
//        CoroutineScope(Dispatchers.IO).launch {
//            session?.incoming
//                ?.consumeAsFlow()
//                ?.filterIsInstance<Frame.Text>()
//                ?.collect {
//                    incomingMessages.emit(it.readText())
//                }
//        }
    }
    override suspend fun subscribe(destination: String) {
        stompClient.topic(destination)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ stompMessage ->
                incomingMessages.tryEmit(stompMessage.payload)
            }, { throwable ->
                Log.e("Stomp", "Error on subscribe", throwable)
            })
    }
//    override suspend fun subscribe(destination: String) {
//        session?.send(Frame.Text("SUBSCRIBE\ndestination:$destination\nid:${destination.hashCode()}\n\n\u0000"))
//    }

//    override suspend fun addUser(
//        destination: String,
//        user: UserDto
//    ) {
//        val json = Json.encodeToString(user)
//        val frame = "SEND\ndestination:$destination\ncontent-type:application/json\n\n$json\u0000"
//        session?.send(Frame.Text(frame))
//    }
        override suspend fun addUser(destination: String, user: UserDto) {
            val json = Json.encodeToString(user)
            stompClient.send(destination, json).subscribe()
        }


//    override suspend fun sendMessage(receipientId: String,message: ChatMessageDto) {
//        val json = Json.encodeToString(message)
//        val frame = "SEND\ndestination:/app/chat\content-type:application/json\n\n$json\u0000"
//        session?.send(Frame.Text(frame))
//    }

    override suspend fun sendMessage(recipientId: String, message: ChatMessageDto) {
        val json = Json.encodeToString(message)
        val sendResult = stompClient.send("/app/chat", json).blockingAwait()
        Log.d("Stomp", "Message send result: $sendResult")
    }

    override fun observeMessages(): Flow<String>  = incomingMessages


//    override suspend fun disconnect() {
//        session?.send(Frame.Text("DISCONNECT\n\n\u0000"))
//        session?.close()
//        client.close()
//
//    }
    override suspend fun disconnect() {
        withContext(Dispatchers.IO) {
            stompClient.disconnect()
        }
    }


}
 *
 */

class KtorStompMessagingClient(
    private val client: HttpClient
) : RealtimeMessagingClient {

    private var subscription: Disposable? = null
    private lateinit var stompClient: StompClient
    private val incomingMessages = MutableSharedFlow<String>(extraBufferCapacity = 64)

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

    override suspend fun connect() = withContext(Dispatchers.IO) {
        stompClient = Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            "ws://$localhost:8088/ws"
        )

         subscription = stompClient.lifecycle().subscribe { event ->
            when (event.type) {
                LifecycleEvent.Type.OPENED -> Log.d("Stomp", "Connected")
                LifecycleEvent.Type.CLOSED -> Log.d("Stomp", "Disconnected")
                LifecycleEvent.Type.ERROR -> Log.e("Stomp", "Error", event.exception)
                else -> {}
            }
        }

        stompClient.connect()
    }

    override suspend fun subscribe(destination: String) {
        withContext(Dispatchers.IO) {
            stompClient.topic(destination).subscribe({ message ->
                incomingMessages.tryEmit(message.payload)
            }, { error ->
                Log.e("Stomp", "Subscribe error", error)
            })
        }
    }

    override suspend fun addUser(destination: String, user: UserDto) {
        val json = Json.encodeToString(user)
        withContext(Dispatchers.IO) {
            suspendCoroutine<Unit> { cont ->
            subscription = stompClient.send(destination, json).subscribe({
                    cont.resume(Unit)
                }, { error ->
                    Log.e("Stomp", "Error adding user", error)
                    cont.resumeWithException(error)
                })
            }
        }
    }


    override suspend fun sendMessage(receipientId: String, message: ChatMessageDto) {
        val json = Json.encodeToString(message)
        withContext(Dispatchers.IO) {
            suspendCoroutine<Unit> { cont ->
           subscription = stompClient.send("/app/chat", json).subscribe({
                    Log.d("Stomp", "Message sent")
                    cont.resume(Unit)
                }, { error ->
                    Log.e("Stomp", "Message failed", error)
                    cont.resumeWithException(error)
                })
            }
        }
    }

    override fun observeMessages(): Flow<String> = incomingMessages

    override suspend fun disconnect() = withContext(Dispatchers.IO) {
        subscription?.dispose()
        stompClient.disconnect()
    }
}
