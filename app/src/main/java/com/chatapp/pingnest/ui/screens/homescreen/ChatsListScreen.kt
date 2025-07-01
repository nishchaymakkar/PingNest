@file:OptIn(ExperimentalMaterial3Api::class)

package com.chatapp.pingnest.ui.screens.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chatapp.pingnest.data.models.Status
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.ui.components.ChatItem
import com.chatapp.pingnest.ui.theme.PingNestTheme

@Preview(apiLevel = 31, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    PingNestTheme {
        ChatsListScreen(
            sender = "",
            isLoading = false,
            users = listOf(
                User(
                    nickName = "john",
                    fullName = "John Doe",
                    status = Status.ONLINE
                )
            ),
            onChatClicked = { _, _ -> },
            onSettingsClicked = {}
        )
    }
}
@Composable
fun ChatsListScreen(
    isLoading: Boolean,
    users: List<User>,
    modifier: Modifier = Modifier,
    onChatClicked:(Int, User)-> Unit,
    onSettingsClicked:()-> Unit,
    sender: String
)  {

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
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
                         val selectedUsers = remember { mutableStateListOf<String>() }
                         val visibleUsers = users.filter { it.nickName != sender }

                         LazyColumn(
                             modifier = Modifier
                                 .fillMaxSize()
                                 .padding(top = 8.dp),
                             horizontalAlignment = Alignment.CenterHorizontally
                         ) {
                             items(visibleUsers) { user ->
                                 val isSelected = selectedUsers.contains(user.nickName)

                                 ChatItem(
                                     nickname = user.nickName,
                                     fullname = user.fullName,
                                     selected = isSelected,
                                     status = user.status,
                                     modifier = Modifier
                                         .fillMaxWidth()
                                         .pointerInput(user.nickName) {
                                             detectTapGestures(
                                                 onLongPress = {
                                                     if (isSelected) {
                                                         selectedUsers.remove(user.nickName)
                                                     } else {
                                                         selectedUsers.add(user.nickName)
                                                     }
                                                 },
                                                 onTap = {
                                                     if (selectedUsers.isNotEmpty()) {
                                                         if (isSelected) {
                                                             selectedUsers.remove(user.nickName)
                                                         } else {
                                                             selectedUsers.add(user.nickName)
                                                         }
                                                     } else {
                                                         val originalIndex = users.indexOfFirst { it.nickName == user.nickName }
                                                         if (originalIndex != -1) {
                                                             onChatClicked(originalIndex, user)
                                                         }
                                                     }
                                                 }
                                             )
                                         }
                                 )
                             }
                         }

        }
    }
}



