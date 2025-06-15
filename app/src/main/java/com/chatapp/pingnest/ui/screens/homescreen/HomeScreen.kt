@file:OptIn(ExperimentalMaterial3Api::class)

package com.chatapp.pingnest.ui.screens.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.chatapp.pingnest.R
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.ui.components.ChatItem
import com.chatapp.pingnest.ui.components.DividerItem
import com.chatapp.pingnest.ui.components.DrawerItemHeader
import com.chatapp.pingnest.ui.theme.PingNestTheme

@Preview(apiLevel = 31, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    PingNestTheme {
        HomeScreen(
            sender = "",
            isLoading = false,
            users = emptyList(),
            onChatClicked = { _, _ -> },
            onLogOut = {}
        )
    }
}
@Composable
fun HomeScreen(
    isLoading: Boolean,
    users: List<User>,
    modifier: Modifier = Modifier,
    onChatClicked:(Int, User)-> Unit,
    onLogOut:()-> Unit,
    sender: String
) {
    var expanded by remember { mutableStateOf(false)}
    Scaffold (
        topBar = {
            PingNestAppBar(
                expanded = expanded,
                onLogOut = onLogOut,
                onExpandededChange = { expanded = it}
            )

        },
    ){ innerPadding ->

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(innerPadding),
        ){
            if (isLoading){

                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

            } else if (users.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonOff,
                        contentDescription = null
                    )
                    Spacer(
                        modifier = Modifier.size(8.dp)
                    )
                    Text("No connected user found")
                }
            } else {
                LazyColumn(
                    Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(users.filter { user -> user.nickName != sender }) { index, user ->
                        ChatItem(
                            nickname = user.nickName,
                            fullname = user.fullName,
                            selected = false,
                            status = user.status,
                            onChatClicked = { onChatClicked(index, user) }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun PingNestAppBar(
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit = {},
    expanded: Boolean,
    onExpandededChange: (Boolean) -> Unit
) {
    var settingsIconPosition by remember { mutableStateOf(Offset.Zero) }

    Column(modifier = modifier.fillMaxWidth()) {
        TopAppBar(
            title = {
                Row {
                    Text(
                        text = "PingNest",
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            },
            navigationIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(50.dp),
                )
            },
            actions = {
                Box {
                    IconButton(
                        onClick = { onExpandededChange(true) },
                        modifier = Modifier.onGloballyPositioned {
                            val position = it.localToWindow(Offset.Zero)
                            settingsIconPosition = position
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { onExpandededChange(false) },
                        offset = DpOffset(x = 0.dp, y = 0.dp), // optional adjustment
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Log out") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = "log out"
                                )
                            },
                            onClick = {
                                onLogOut()
                                onExpandededChange(false)
                            }
                        )
                    }
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth().align(Alignment.End)
                .padding(horizontal = 4.dp),
        ) {

            DrawerItemHeader(text = "Connected Users")
            DividerItem()
        }
    }
}
