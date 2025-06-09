@file:OptIn(ExperimentalMaterial3Api::class)

package com.chatapp.pingnest.ui.screens.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BookmarkBorder
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chatapp.pingnest.R
import com.chatapp.pingnest.data.models.Status
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.ui.components.ChatItem
import com.chatapp.pingnest.ui.components.DividerItem
import com.chatapp.pingnest.ui.components.DrawerItemHeader
import com.chatapp.pingnest.ui.theme.PingNestTheme

@Preview(apiLevel = 34)
@Composable
private fun HomeScreenPreview() {
    PingNestTheme {
//        HomeScreen()
    }
}
@Composable
fun HomeScreen(
    isLoading: Boolean,
    users: List<User>,
    modifier: Modifier = Modifier,
    onChatClicked:(Int, User)-> Unit = {_, _ -> },
    onLogOut:()-> Unit,
    sender: String
) {
    var isSettingsClicked by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false)}
    Scaffold (
        topBar = {
            PingNestAppBar(
                onSettingsClicked = {
                    isSettingsClicked = !isSettingsClicked
                    expanded = !expanded
                },
                expanded = expanded,
                isSettingsClicked = isSettingsClicked,
                onLogOut = {
                    onLogOut()
                expanded = !expanded
                }
            )

        },
    ){ innerPadding ->

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(innerPadding),
        ){

            Column (
                modifier = Modifier.fillMaxSize(),
            ){

            //DividerItem()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
            ) {

                DrawerItemHeader(text = "Connected Users")
                DividerItem()
                LazyColumn(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isLoading){
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    } else if (users.isEmpty()){
                        item{
                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ){
                                Column (
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                    Icon(
                                        imageVector = Icons.Default.PersonOff,
                                        contentDescription = null
                                    )
                                    Spacer(
                                        modifier = Modifier.size(8.dp)
                                    )
                                    Text("No connected user found")
                                }

                            }

                        }
                    } else {
                        itemsIndexed (users.filter { user-> user.nickName != sender  }) { index, user ->
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
    }
}


@Composable
private fun PingNestAppBar(
    modifier: Modifier = Modifier,
    onSettingsClicked: () -> Unit = {},
    isSettingsClicked: Boolean,
    expanded: Boolean,
    onLogOut: () -> Unit = {}
) {
    Row {
    TopAppBar(
        title = {
            Row {
                Text(
                    text = "PingNest",
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        navigationIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(50.dp),
            )
        },
        actions = {
            IconButton(
                onClick = onSettingsClicked
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null
                )
            }
        }
    )
        if (isSettingsClicked){

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {  }
                ) {
                    // Update the DropdownMenuItem section
                    DropdownMenuItem(
                        text = { Text("Log out") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "log out"
                            )
                        },
                        onClick = onLogOut
                    )

                }
            }
        }
    }
}