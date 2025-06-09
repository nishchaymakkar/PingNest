@file:OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)

package com.chatapp.pingnest.ui.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.VerticalDragHandle
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.util.Locale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chatapp.pingnest.data.models.ChatMessage
import com.chatapp.pingnest.ui.PingNestViewModel
import com.chatapp.pingnest.ui.screens.chatroom.ChatRoom
import com.chatapp.pingnest.ui.screens.homescreen.HomeScreen
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID


@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun UserListAndChatRoom(
    viewModel: PingNestViewModel
) {
    val users by viewModel.users.collectAsStateWithLifecycle()
    val uiState = viewModel.state
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    var selectedUserIndex: Int? by rememberSaveable { mutableStateOf(null) }
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    /**
     * selectedUser needs to be in viewModel otherwise it will make app crash
     **/
    var selectedUser = viewModel.user.value
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val scope = rememberCoroutineScope()
    val isListAndDetailVisible = navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded
            && navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val formattedTime = dateFormat.format(Date())

    BackHandler(enabled = navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
            viewModel.removeUser()
            selectedUserIndex = null

        }
    }
    val senderName by viewModel.nickname.collectAsStateWithLifecycle()
    SharedTransitionLayout {
        AnimatedContent(targetState = isListAndDetailVisible, label = "users & chat") {
            ListDetailPaneScaffold(
                directive = navigator.scaffoldDirective,
                value = navigator.scaffoldValue,
                listPane = {
                    AnimatedPane {
                        HomeScreen(
                            isLoading = isLoading,
                            users = users,
                            onChatClicked = { index, user ->
                                viewModel.setUser(user)
                                selectedUserIndex = index
                                viewModel.getMessages(senderId = senderName ?: "unknown", recipientId = user.nickName)
                                viewModel.subscribe("/user/queue/messages")
                                scope.launch {
                                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                                }
                            },
                            onLogOut = {
                                viewModel.logout()
                            },
                            sender = senderName.toString()
                        )
                    }
                },
                detailPane = {
                        viewModel.observeMessages()
                    AnimatedPane {
                        ChatRoom(
                            user = selectedUser,
                            messages = messages,
                            onNavIconPressed = {
                                scope.launch {
                                    navigator.navigateBack()
                                   viewModel.removeUser()
                                }
                            },
                            onSend = {
                                viewModel.getMessages(senderName.toString(),selectedUser?.nickName.toString())
                                viewModel.send(
                                    recipientId = selectedUser?.nickName.toString(),
                                    message = ChatMessage(
                                        id = selectedUser?.nickName.toString(),
                                        chatId = UUID.randomUUID().toString(),
                                        senderId = senderName ?: "unknown",
                                        recipientId = selectedUser?.nickName.toString(),
                                        content = viewModel.message,
                                        timestamp = formattedTime
                                    )
                                )

                            },
                            message = viewModel.message,
                            onMessageChange = viewModel::onMessageChange,
                            sender = senderName.toString()
                        )
                    }
                },
                paneExpansionState = rememberPaneExpansionState(navigator.scaffoldValue),
                paneExpansionDragHandle = { state->
                    val interactionSource = remember { MutableInteractionSource() }
                    VerticalDragHandle(
                        modifier =
                            Modifier.paneExpansionDraggable(
                                state = state,
                                minTouchTargetSize = LocalMinimumInteractiveComponentSize.current,
                                interactionSource = interactionSource,
                                semanticsProperties = {
                                }
                            ), interactionSource = interactionSource
                    )
                }
            )
        }
    }
}