package com.chatapp.pingnest.ui.screens.homescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chatapp.pingnest.data.models.Status
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.ui.components.ChatItem
import com.chatapp.pingnest.ui.components.DividerItem
import com.chatapp.pingnest.ui.components.DrawerHeader
import com.chatapp.pingnest.ui.components.DrawerItemHeader
import com.chatapp.pingnest.ui.theme.PingNestTheme
val users = listOf(
    User(nickName = "skywalker", fullName = "Luke Skywalker", status = Status.ONLINE),
    User(nickName = "darthvader", fullName = "Anakin Skywalker", status = Status.OFFLINE),
    User(nickName = "leia", fullName = "Leia Organa", status = Status.ONLINE),
    User(nickName = "han_solo", fullName = "Han Solo", status = Status.OFFLINE),
    User(nickName = "yoda", fullName = "Master Yoda", status = Status.ONLINE),
    User(nickName = "obiwan", fullName = "Obi-Wan Kenobi", status = Status.OFFLINE),
    User(nickName = "chewie", fullName = "Chewbacca", status = Status.ONLINE)
)

@Preview(apiLevel = 34)
@Composable
private fun HomeScreenPreview() {
    PingNestTheme {
        HomeScreen()
    }
}
@Composable
fun HomeScreen(modifier: Modifier = Modifier,onChatClicked:(Int, User)-> Unit = {_, _ -> }) {
    Scaffold { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ){
            Column (
                modifier = Modifier.fillMaxSize(),
            ){
            DrawerHeader(modifier)
            //DividerItem()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {

                DrawerItemHeader(text = "Connected Users")
                DividerItem()
                LazyColumn {
                    itemsIndexed (users){index,user->
                        ChatItem(
                            nickname = user.nickName,
                            fullname = user.fullName,
                            selected = false,
                            status = user.status,
                            onChatClicked = { onChatClicked(index,user) }
                        )

                    }
                }

            }
            }
        }
    }
}