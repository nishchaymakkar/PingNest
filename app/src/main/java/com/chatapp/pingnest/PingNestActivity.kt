package com.chatapp.pingnest

import EnterChatRoomDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.chatapp.pingnest.ui.screens.UserListAndChatRoom
import com.chatapp.pingnest.ui.screens.homescreen.HomeScreen
import com.chatapp.pingnest.ui.theme.PingNestTheme

class PingNestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PingNestTheme {
//                EnterChatRoomDialog(
//                    onDismiss = {  },
//                    onEnter = { nickname, realName ->  }
//                )
                UserListAndChatRoom()
            }
        }
    }
}

