package com.chatapp.pingnest

import EnterChatRoomDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.chatapp.pingnest.data.models.AppTheme
import com.chatapp.pingnest.data.models.Status
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.ui.PingNestApp
import com.chatapp.pingnest.ui.PingNestViewModel
import com.chatapp.pingnest.ui.theme.PingNestTheme
import org.koin.androidx.compose.koinViewModel

class PingNestActivity : ComponentActivity() {

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = koinViewModel<PingNestViewModel>()
            val savedTheme = viewModel.themeFlow.collectAsStateWithLifecycle(initialValue = AppTheme.SYSTEM_DEFAULT).value
            val state = viewModel.state
            val isUserPresent by viewModel.isUserPresent.collectAsStateWithLifecycle()
            LaunchedEffect(
                Unit
            ) {
                viewModel.connect()
            }
            PingNestTheme(
                darkTheme = when(savedTheme){
                    AppTheme.LIGHT -> false
                    AppTheme.DARK -> true
                    AppTheme.SYSTEM_DEFAULT -> isSystemInDarkTheme()
                }
            ) {
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
                    PingNestApp(
                        viewModel = viewModel
                    )
                }
                }
            }
            DisposableEffect(Unit) {
                onDispose {
                    viewModel.disconnect()
                }
            }
        }
    }


}

