package com.chatapp.pingnest

import EnterChatRoomDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chatapp.pingnest.data.models.Status
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.ui.PingNestViewModel
import com.chatapp.pingnest.ui.screens.UserListAndChatRoom
import com.chatapp.pingnest.ui.theme.PingNestTheme
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

class PingNestActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = koinViewModel<PingNestViewModel>()

            val state = viewModel.state
            val isUserPresent by viewModel.isUserPresent.collectAsStateWithLifecycle()
            LaunchedEffect(
                Unit
            ) {
                viewModel.connect()
            }
            PingNestTheme {
                Surface {
                    if (isUserPresent){
                        viewModel.userPresent()
                    }
                if (state.isConnecting){
                    EnterChatRoomDialog(
                    onDismiss = {  },
                        nickname = state.nickname,
                    fullname = state.fullname,
                    onNicknameChange = { viewModel.onNicknameChange(it) },
                    onRealNameChange = { viewModel.onRealNameChange(it) },
                    onEnter = {

                        val fullName = state.fullname.trim()
                        val nickName = state.nickname.trim()
                        if (fullName.isNotBlank() && nickName.isNotBlank()){
                            viewModel.saveUserLocally(fullName = fullName, nickname = nickName)
                            viewModel.addUser(
                                destination = getString(R.string.userDestination),
                                user = User(
                                    nickName = nickName,
                                    fullName = fullName,
                                    status = Status.ONLINE
                                )
                            )
                            viewModel.subscribe(getString(R.string.usertopic))
                        }






                    }
                ) } else {
                    UserListAndChatRoom(
                        viewModel = viewModel
                    )
                }
                }
            }
            DisposableEffect(Unit) {
                viewModel.disconnect()
                onDispose {
                    viewModel.disconnect()
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

