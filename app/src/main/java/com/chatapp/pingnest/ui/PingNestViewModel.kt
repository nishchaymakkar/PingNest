package com.chatapp.pingnest.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatapp.pingnest.data.local.DataStoreRepository
import com.chatapp.pingnest.data.models.ChatMessage
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.data.network.RealtimeMessagingClient
import com.chatapp.pingnest.ui.mappers.toChatMessage
import com.chatapp.pingnest.ui.mappers.toChatMessageDto
import com.chatapp.pingnest.ui.mappers.toUser
import com.chatapp.pingnest.ui.mappers.toUserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


class PingNestViewModel(
    private val messagingClient: RealtimeMessagingClient,
    private val dataStoreRepository: DataStoreRepository
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

    val isUserPresent = dataStoreRepository.currentUser()
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserState()
    )
    fun setUser(newUser: User) {
        _user.value = newUser
    }
    fun removeUser(){
        _user.value = null
    }
    fun userPresent() {
        viewModelScope.launch {
            state = state.copy(
                isConnecting = false
            )
        }
    }
    fun saveUserLocally(fullName: String, nickname: String){
        viewModelScope.launch {
            dataStoreRepository.saveUser(fullName, nickname)
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
            _users.value = messagingClient.getUsers().map { it ->
                it.toUser()
            }
        }
    }
    fun getMessages(senderId: String, recipientId: String){
        viewModelScope.launch {
            _messages.value = messagingClient.getMessages(senderId, recipientId).map {
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
    fun send(recipientId: String,message: ChatMessage){
        viewModelScope.launch {
            messagingClient.sendMessage( recipientId,message.toChatMessageDto())

        }
    }
//    fun observeMessages(){
//        viewModelScope.launch {
//            messagingClient.observeMessages().collect{ message ->
//                _messages.value = messages.value.map { it.copy(content = message) }
//            }
//        }
//    }


}

val viewModelModule = module {
    viewModel { PingNestViewModel(get(),get()) }

}