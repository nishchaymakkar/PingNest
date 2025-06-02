package com.chatapp.pingnest.ui.screens.homescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chatapp.pingnest.ui.components.ChatItem
import com.chatapp.pingnest.ui.components.DividerItem
import com.chatapp.pingnest.ui.components.DrawerHeader
import com.chatapp.pingnest.ui.components.DrawerItemHeader
import com.chatapp.pingnest.ui.theme.PingNestTheme

@Preview
@Composable
private fun HomeScreenPreview() {
    PingNestTheme {
        HomeScreen()
    }
}
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Scaffold { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ){
            Column (
                modifier = Modifier.fillMaxSize(),
            ){
            DrawerHeader()
            DividerItem()
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {

                DrawerItemHeader(text = "Connected Users")
                DividerItem()
                ChatItem(
                    text = "Nishchay",
                    selected = true,
                    onChatClicked = { }
                )
                ChatItem(
                    text = "Nishchay",
                    selected = false,
                    onChatClicked = {}
                )
            }
            }
        }
    }
}