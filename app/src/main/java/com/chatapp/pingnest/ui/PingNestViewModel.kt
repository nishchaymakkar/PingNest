package com.chatapp.pingnest.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatapp.pingnest.data.models.AppSettings
import com.chatapp.pingnest.data.models.AppTheme
import com.chatapp.pingnest.data.models.ChatMessage
import com.chatapp.pingnest.data.models.ChatThemeType
import com.chatapp.pingnest.data.models.ChatWallpaperType
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.data.network.PingNestApiService
import com.chatapp.pingnest.data.network.RealtimeMessagingClient
import com.chatapp.pingnest.ui.mappers.toChatMessage
import com.chatapp.pingnest.ui.mappers.toChatMessageDto
import com.chatapp.pingnest.ui.mappers.toUser
import com.chatapp.pingnest.ui.mappers.toUserDto
import com.chatapp.pingnest.ui.photopicker.PhotoPickerViewModel
import com.chatapp.pingnest.ui.screens.chatroom.ChatRoomViewModel
import com.chatapp.pingnest.ui.screens.settings.SettingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class PingNestViewModel(
    private val apiClient: PingNestApiService,
    private val messagingClient: RealtimeMessagingClient,
    private val appSettings: DataStore<AppSettings>
): ViewModel() {
    var state by mutableStateOf(UserState())
        private set
    var message by mutableStateOf<String>("")
        private set
    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading
        .onStart {
            getUsers()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

//    val userName = userRepository.getFullName().stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.Eagerly,
//        initialValue = ""
//    )
//    val userNickname = userRepository.getNickName().stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.Eagerly,
//        initialValue = ""
//    )



    val userName = appSettings.data.map { it.userData.fullName }
    val userNickname = appSettings.data.map { it.userData.nickName }
    val isUserPresent = combine(userName, userNickname) { name, nick ->
        name.isNotBlank() && nick.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    fun setUser(newUser: User) {
        _user.value = newUser
    }
    fun removeUser(){
        _user.value = null
    }

    @SuppressLint("NewApi")
    fun getCurrentTimestamp(): String {
        val now = LocalDateTime.now()
        return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }

    fun userPresent() {
        viewModelScope.launch {
            state = state.copy(
                isConnecting = false
            )

            if (userName.first() != "" && userNickname.first() != ""){
            messagingClient.addUser(
                destination = "/app/user.addUser",
                user = User(
                    nickName = userNickname.first(),
                    fullName = userName.first(),
                ).toUserDto()

            )
            messagingClient.subscribe("/topic/user")

            }


        }
    }
    fun saveUserLocally(fullName: String, nickname: String){
        viewModelScope.launch {
//            userRepository.saveFullName(fullName)
//            userRepository.saveNickName(nickname)

            appSettings.updateData {
                it.copy(
                    userData = User(
                        fullName = fullName,
                        nickName = nickname
                    ))}

        }
    }

    fun onMessageChange(message: String){
        this.message = message
    }
    fun onNicknameChange(nickname: String){
        state = state.copy(
            nickname = nickname
        )
    }
    fun onRealNameChange(realName: String){
        state = state.copy(
            fullname = realName
        )
    }

    fun getUsers(){
        viewModelScope.launch {
            _users.value = apiClient.getUsers().map { it ->
                it.toUser()
            }
        }
    }
    fun getMessages(senderId: String, recipientId: String){
        viewModelScope.launch {
            _messages.value = apiClient.getMessages(senderId, recipientId).map {
                it.toChatMessage()
            }
        }
    }
    fun connect(){
        viewModelScope.launch {
            messagingClient.connect()
        }
    }
    fun disconnect(){
        viewModelScope.launch {
            messagingClient.disconnect()

            if (userName.first() != "" && userNickname.first() != ""){
            messagingClient.addUser(
                destination = "/app/user.disconnectUser",
                user = User(
                    nickName = userNickname.first(),
                    fullName = userName.first(),
                ).toUserDto()
            )
        }
        }
    }
    fun subscribe(destination: String){
        viewModelScope.launch {
            messagingClient.subscribe(destination)
        }
    }

    fun addUser(destination: String, user: User){
        state = state.copy(
            isConnecting = false
        )
        viewModelScope.launch {
            messagingClient.addUser(destination, user.toUserDto())
        }
    }
    init {
      viewModelScope.launch {
        delay(4000)
          if (userNickname.first() != ""){
          subscribeMessages(userId = userNickname.first())
          } else {
              println("User nickname is blank")
          }

      }
    }
    fun subscribeMessages(userId: String){
        viewModelScope.launch {
        messagingClient.subscribe("/user/$userId/queue/messages")
            delay(2000)
            observeMessages()
        }

    }
    fun send(recipientId: String, senderId: String){
        val messageToSend = message.trim()
        if (messageToSend.isNotBlank()){
        val chatMessage =  ChatMessage(
            senderId = senderId,
            recipientId = recipientId,
            content = messageToSend,
            timestamp = getCurrentTimestamp()
        )
        viewModelScope.launch {
            messagingClient.sendMessage(chatMessage.toChatMessageDto())
            _messages.update { it + chatMessage }
        }
        message = ""
        }
    }

    fun observeMessages(){
        Log.d("PingNestViewModel","Observing Messages")
        viewModelScope.launch {
            messagingClient.observeMessages().collect{ json ->
                _messages.update { it + ChatMessage(
                    senderId = json.senderId,
                    recipientId = json.recipientId,
                    content = json.content,
                    id = json.id,
                    timestamp = getCurrentTimestamp()
                ) }
                Log.d("PingNestViewModel", "Received message: $json")
            }

        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            messagingClient.disconnect()
        }
        super.onCleared()
    }

    val themeFlow = appSettings.data.map { it.theme }
}

