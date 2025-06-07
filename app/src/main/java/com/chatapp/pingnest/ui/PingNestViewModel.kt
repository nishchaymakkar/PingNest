package com.chatapp.pingnest.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatapp.pingnest.data.models.ChatMessage
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.data.network.RealtimeMessagingClient
import com.chatapp.pingnest.ui.mappers.toChatMessage
import com.chatapp.pingnest.ui.mappers.toChatMessageDto
import com.chatapp.pingnest.ui.mappers.toUser
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
    private val messagingClient: RealtimeMessagingClient
): ViewModel() {
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
    fun send(destination: String, message: ChatMessage){
        viewModelScope.launch {
            messagingClient.send( message.toChatMessageDto())

        }
    }
    fun observeMessages(){
        viewModelScope.launch {
            messagingClient.observeMessages().collect{ message ->
                _messages.value = messages.value.map { it.copy(content = message) }
            }
        }
    }


}

val viewModelModule = module {
    viewModel { PingNestViewModel(get()) }

}