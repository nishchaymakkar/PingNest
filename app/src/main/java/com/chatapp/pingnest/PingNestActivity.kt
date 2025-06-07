package com.chatapp.pingnest

import EnterChatRoomDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import com.chatapp.pingnest.data.models.Status
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.ui.PingNestViewModel
import com.chatapp.pingnest.ui.screens.UserListAndChatRoom
import com.chatapp.pingnest.ui.screens.homescreen.HomeScreen
import com.chatapp.pingnest.ui.theme.PingNestTheme
import org.koin.androidx.compose.koinViewModel

class PingNestActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = koinViewModel<PingNestViewModel>()

            val state = viewModel.state
            LaunchedEffect(
                Unit
            ) {
                viewModel.connect()
            }
            PingNestTheme {
                Surface {
                if (state.isConnecting){
                    EnterChatRoomDialog(
                    onDismiss = {  },
                        nickname = state.nickname,
                    fullname = state.fullname,
                    onNicknameChange = { viewModel.onNicknameChange(it) },
                    onRealNameChange = { viewModel.onRealNameChange(it) },
                    onEnter = {
                        viewModel.addUser(
                            destination = "/app/user.addUser",
                            user = User(
                                nickName = state.nickname,
                                fullName = state.fullname,
                                status = Status.ONLINE
                            )
                        )
                        viewModel.subscribe("/user/queue/messages")


                    }
                ) } else {
                    UserListAndChatRoom(
                        viewModel = viewModel
                    )
                }
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        setContent {
            val viewModel = koinViewModel<PingNestViewModel>()
                viewModel.disconnect()
        }

    }
}

