@file:OptIn(ExperimentalMaterial3Api::class)

package com.chatapp.pingnest.ui.screens.homescreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.chatapp.pingnest.R
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.ui.PingNestViewModel
import com.chatapp.pingnest.ui.components.FunctionalityNotAvailablePopup
import com.chatapp.pingnest.ui.navigation.BottomBarScreenSaver
import com.chatapp.pingnest.ui.navigation.NavigationBarItem
import com.chatapp.pingnest.ui.navigation.bottomBarItems
import com.chatapp.pingnest.ui.screens.timeline.TimelineScreen

@Composable
fun HomeNavGraph(
    onSettingsClicked: () -> Unit,
    viewModel: PingNestViewModel,
    onChatClicked: (User) -> Unit
) {
    val backStack = rememberNavBackStack(NavigationBarItem.ChatList)
    var expanded by remember { mutableStateOf(false)}
    var popUp by remember { mutableStateOf(false) }
    var currentScreen: NavigationBarItem by rememberSaveable(
        stateSaver = BottomBarScreenSaver
    ) { mutableStateOf(NavigationBarItem.ChatList) }
    val users by viewModel.users.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val senderName by viewModel.userNickname.collectAsStateWithLifecycle(initialValue = "")

    Scaffold (
        topBar = {
            PingNestAppBar(
                expanded = expanded,
                onSettingsClicked = onSettingsClicked,
                onExpandedChange = { expanded = it}
            )
        },
        bottomBar = {
            NavigationBar (
                windowInsets = WindowInsets.displayCutout,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                bottomBarItems.forEach { screen ->
                    val isSelected = currentScreen == screen
                    NavigationBarItem(
                        selected = isSelected,
                        icon = {
                            Icon(
                               painter = if (isSelected) painterResource( screen.selectedIcon) else painterResource(screen.unSelectedIcon),
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(.7f)
                            )

                        },
                        label = {
                            Text(
                                text = screen.title,
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface.copy(.7f),
                                fontWeight = FontWeight.Bold
                            )
                        },
                        onClick = {
                            if (backStack.lastOrNull() != screen){
                                if (backStack.lastOrNull() in bottomBarItems) {
                                    backStack.removeAt(backStack.lastIndex)
                                }
                                backStack.add(screen)
                                currentScreen = screen
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f),
                        )
                    )
                }
            }

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {popUp = !popUp},
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Message,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        if (popUp){
            FunctionalityNotAvailablePopup(onDismiss = {popUp = !popUp})
        }
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            backStack = backStack,
            onBack = {backStack.removeLastOrNull()},
            entryProvider = entryProvider {
                entry<NavigationBarItem.ChatList>{
                    ChatsListScreen(
                        isLoading = isLoading,
                        users = users,
                        onChatClicked = { index, user ->
                            viewModel.getMessages(
                                senderId = senderName ?: "unknown",
                                recipientId = user.nickName
                            )
                            onChatClicked(user)

                        },
                        onSettingsClicked = onSettingsClicked,
                        sender = senderName.toString()
                    )
                }
                entry<NavigationBarItem.TimeLine> {
                    TimelineScreen()
                }
            }
        )
    }
}

@Composable
private fun PingNestAppBar(
    onSettingsClicked: () -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {

    TopAppBar(
        modifier = Modifier,
        title = {
            Row {
                Text(
                    text = stringResource(R.string.app_name),
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        navigationIcon = {
//                Icon(
//                    painter = painterResource(R.drawable.ic_launcher_foreground),
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.primary,
//                    modifier = Modifier.size(50.dp),
//                )
        },
        actions = {

            IconButton(
                onClick = { onExpandedChange(true) },
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            DropdownMenu(
                expanded = expanded,
                shape = RoundedCornerShape(8.dp),
                onDismissRequest = { onExpandedChange(false) },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                DropdownMenuItem(
                    text = { Text("Settings") },
                    onClick = {
                        onSettingsClicked()
                        onExpandedChange(false)
                    }
                )
            }




        }
    )

}