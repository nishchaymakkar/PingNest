package com.chatapp.pingnest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chatapp.pingnest.data.models.ChatMessage
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.ui.PingNestViewModel
import com.chatapp.pingnest.ui.screens.chatroom.ChatRoom
import com.chatapp.pingnest.ui.screens.chatroom.ChatRoomViewModel
import com.chatapp.pingnest.ui.theme.PingNestTheme
import org.koin.androidx.compose.koinViewModel

class BubbleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       val recipient = intent.getStringExtra("sender")
        setContent {

            val viewModel = koinViewModel<PingNestViewModel>()
            LaunchedEffect(
                Unit
            ) { viewModel.getUsers()}
            val messages by viewModel.messages.collectAsStateWithLifecycle()
            val users by viewModel.users.collectAsStateWithLifecycle()
            val user = recipient?.let { recipientId ->
                users.firstOrNull { user ->
                    user.nickName.trim().equals(recipientId.trim(), ignoreCase = true)
                }
            } ?: User("unknown", "unknown")
            val senderId by viewModel.userNickname.collectAsStateWithLifecycle(initialValue = "")
            if (senderId != ""){
                LaunchedEffect(Unit) {
                    viewModel.getMessages(
                        senderId = senderId,
                        recipientId = recipient ?: ""
                    )
                }

                PingNestTheme {
                    Bubble(
                        user = user,
                        messages = messages,
                        onSend = {
                            viewModel.send(
                                senderId = senderId,
                                recipientId = recipient ?: ""
                            )
                        },
                        onMessageChange = viewModel::onMessageChange,
                        sender = senderId
                    )
                }
            }

        }
    }


}

@Composable
fun Bubble(user: User,sender: String, messages: List<ChatMessage>, onSend: ()-> Unit, onMessageChange: (String) -> Unit ) {
    val chatRoomViewmodel = koinViewModel<ChatRoomViewModel>()
    ChatRoom(
        user = user,
        sender = sender,
        messages = messages,
        onNavIconPressed = { },
        onSend = onSend,
        onMessageChange = onMessageChange,
        onPhotoPickerClicked = { },
        onCameraClicked = {},
        viewModel = chatRoomViewmodel,
        onVideoClicked = {}
    )

}