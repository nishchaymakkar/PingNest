@file:OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3WindowSizeClassApi::class)

package com.chatapp.pingnest.ui

import android.util.Log
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.HingePolicy
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.media3.common.util.UnstableApi
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.chatapp.pingnest.R
import com.chatapp.pingnest.ui.camera.Camera
import com.chatapp.pingnest.ui.camera.Media
import com.chatapp.pingnest.ui.camera.MediaType
import com.chatapp.pingnest.ui.navigation.CameraScreen
import com.chatapp.pingnest.ui.navigation.ChatRoom
import com.chatapp.pingnest.ui.navigation.HomeScreen
import com.chatapp.pingnest.ui.navigation.NavigationBarItem
import com.chatapp.pingnest.ui.navigation.PhotoPicker
import com.chatapp.pingnest.ui.navigation.Settings
import com.chatapp.pingnest.ui.navigation.VideoEditor
import com.chatapp.pingnest.ui.photopicker.PhotoPicker
import com.chatapp.pingnest.ui.screens.chatroom.ChatRoom
import com.chatapp.pingnest.ui.screens.homescreen.ChatsListScreen
import com.chatapp.pingnest.ui.screens.homescreen.HomeNavGraph
import com.chatapp.pingnest.ui.screens.settings.SettingsNavigation
import com.chatapp.pingnest.ui.videoedit.VideoEditScreen


@UnstableApi
@Composable
fun PingNestApp(
    modifier: Modifier = Modifier,
    viewModel: PingNestViewModel
) {

    val backStack = rememberNavBackStack(NavigationBarItem.ChatList)
    val listAndDetail = rememberListDetailSceneStrategy<NavKey>(
        backNavigationBehavior = BackNavigationBehavior.PopUntilCurrentDestinationChange,
        directive = calculatePaneScaffoldDirective(
            windowAdaptiveInfo = currentWindowAdaptiveInfo(),
            verticalHingePolicy = HingePolicy.NeverAvoid
        )
    )

    val users by viewModel.users.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val senderName by viewModel.userNickname.collectAsStateWithLifecycle(initialValue = "")

    NavDisplay(
        modifier = modifier.windowInsetsPadding(WindowInsets.displayCutout),
        backStack = backStack,
        sceneStrategy = listAndDetail,
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavigationBarItem.ChatList>{
                HomeNavGraph(
                    onSettingsClicked = {
                        backStack.add(Settings.SettingsScreen)
                    },
                    onChatClicked = { user->
                        val last = backStack.lastOrNull()
                        if (last is ChatRoom) {
                            backStack[backStack.lastIndex] = ChatRoom(user)
                        } else {
                            backStack.add(ChatRoom(user))
                        }
                    },
                    viewModel = viewModel
                )
            }
            entry(HomeScreen,
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                            Text(
                                text = stringResource(R.string.app_name),
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                                )
                        }

                    }))  {
                ChatsListScreen(
                    isLoading = isLoading,
                    users = users,
                    onChatClicked = { index, user ->
                        Log.d("HomeScreen", "Chat clicked with user: ${user.nickName}")
                        viewModel.getMessages(
                            senderId = senderName ?: "unknown",
                            recipientId = user.nickName
                        )
                        backStack.add(ChatRoom(user))
                        val last = backStack.lastOrNull()
                        if (last is ChatRoom) {
                            backStack[backStack.lastIndex] = ChatRoom(user)
                        } else {
                            backStack.add(ChatRoom(user))
                        }
                    },
                    onSettingsClicked = {backStack.add(Settings.SettingsScreen)},
                    sender = senderName.toString()
                )
            }
            entry<ChatRoom>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { args ->
                ChatRoom(
                    user = args.user,
                    messages = messages,
                    onNavIconPressed = {
                        backStack.removeLastOrNull()
                    },
                    onSend = {
                        viewModel.send(
                            recipientId = args.user.nickName,
                            senderId = senderName.toString()
                        )
                    },
                    onMessageChange = viewModel::onMessageChange,
                    sender = senderName.toString(),
                    onCameraClicked = {backStack.add(CameraScreen(args.user))},
                    onPhotoPickerClicked = {backStack.add(PhotoPicker)}
                    )

            }
            entry(PhotoPicker) {
                PhotoPicker(
                    onPhotoPicked = { backStack.removeLastOrNull() }
                )
            }
            entry<CameraScreen>{ args->
               Camera(
                   onMediaCaptured = { capturedMedia: Media? ->
                       when(capturedMedia?.mediaType){
                           MediaType.PHOTO -> {
                               backStack.removeLastOrNull()
                           }
                           MediaType.VIDEO -> {
                               backStack.add(VideoEditor(args.user,capturedMedia.uri.toString()))
                           }
                           else -> {
                               backStack.removeLastOrNull()
                           }
                       }

                   }
               )
            }
            entry<VideoEditor>{args ->
                VideoEditScreen(
                    uri = args.uri,
                    onCloseButtonClicked = {backStack.removeLastOrNull()},
                    onSendButtonClicked = {
                        backStack.removeRange(
                            backStack.size - 2,
                            backStack.size
                        )
                    },
                )

            }
            entry(Settings.SettingsScreen) {
               SettingsNavigation(
                   onNavBackClicked = {
                       backStack.removeLastOrNull()
                   },
               )
            }

        },
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
        },
        popTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        }

    )
}

