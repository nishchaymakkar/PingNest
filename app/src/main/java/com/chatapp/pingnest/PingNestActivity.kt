package com.chatapp.pingnest

import EnterChatRoomDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
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
            PingNestTheme {
//                EnterChatRoomDialog(
//                    onDismiss = {  },
//                    onEnter = { nickname, realName ->  }
//                )
                Surface {
                    UserListAndChatRoom(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

