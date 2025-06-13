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
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chatapp.pingnest.R
import com.chatapp.pingnest.ui.PingNestViewModel
import com.chatapp.pingnest.ui.screens.chatroom.ChatRoom
import com.chatapp.pingnest.ui.screens.homescreen.HomeScreen
import kotlinx.coroutines.launch


@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun UserListAndChatRoom(
    viewModel: PingNestViewModel
) {
    val users by viewModel.users.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    var selectedUserIndex: Int? by rememberSaveable { mutableStateOf(null) }
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    var selectedUser = viewModel.user.value
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val scope = rememberCoroutineScope()
    val isListAndDetailVisible = navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded
            && navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded
    val context = LocalContext.current
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
                                viewModel.onChatOpen()
                                selectedUserIndex = index
                                viewModel.getMessages(senderId = senderName ?: "unknown", recipientId = user.nickName)
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
                                   viewModel.send(
                                    recipientId = selectedUser?.nickName.toString(),
                                    senderId = senderName.toString()
                                )
                                viewModel.getMessages(senderName.toString(),selectedUser?.nickName.toString())


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