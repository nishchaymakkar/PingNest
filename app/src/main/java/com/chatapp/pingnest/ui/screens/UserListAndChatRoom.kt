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
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.ui.screens.chatroom.ChatRoom
import com.chatapp.pingnest.ui.screens.homescreen.HomeScreen
import kotlinx.coroutines.launch


@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun UserListAndChatRoom() {
    var selectedUserIndex: Int? by rememberSaveable { mutableStateOf(null) }
    /**
     * selectedUser needs to be in viewModel otherwise it will make app crash
     **/
    var selectedUser: User? by rememberSaveable { mutableStateOf(null) }
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val scope = rememberCoroutineScope()
    val isListAndDetailVisible = navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded
            && navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded
    BackHandler(enabled = navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
            selectedUser = null
            selectedUserIndex = null

        }
    }
    SharedTransitionLayout {
        AnimatedContent(targetState = isListAndDetailVisible, label = "users & chat") {
            ListDetailPaneScaffold(
                directive = navigator.scaffoldDirective,
                value = navigator.scaffoldValue,
                listPane = {AnimatedPane {
                    HomeScreen(onChatClicked = { index, user ->
                        selectedUser = user
                        selectedUserIndex = index
                        scope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                        }
                    })
                }},
                detailPane = {

                    AnimatedPane {
                    ChatRoom(
                        user = selectedUser,
                        onNavIconPressed = {
                            scope.launch {
                                navigator.navigateBack()
                                selectedUser = null
                            }
                        }
                    )
                }},
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