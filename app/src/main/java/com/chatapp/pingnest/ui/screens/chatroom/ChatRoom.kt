@file:OptIn(ExperimentalMaterial3Api::class)

package com.chatapp.pingnest.ui.screens.chatroom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.chatapp.pingnest.data.models.ChatMessage
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.ui.components.JumpToBottom
import com.chatapp.pingnest.ui.components.MessageTextField
import com.chatapp.pingnest.ui.components.ProfileIcon
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID


@Composable
fun ChatRoom(
    modifier: Modifier = Modifier,
    user: User?,
    onNavIconPressed: () -> Unit
) {

    if(user != null){
    Scaffold(
        topBar = {
            ChatNameBar(
                fullname = user.fullName,
                nickname = user.nickName,
                modifier = Modifier,
                onNavIconPressed = onNavIconPressed,
            )
        }
    ) { innerPadding->

        Column(modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(innerPadding)) {

            Messages(
                messages = generateSampleMessages(),
                scrollState = rememberLazyListState(),
                modifier = Modifier.weight(1f)
            )
            MessageTextField(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 8.dp),
                text = "Nishchay Makkar",
                onTextChange = {}
            )
        }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatNameBar(
    fullname: String,
    nickname: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavIconPressed: () -> Unit = { },
) {

    ChatRoomAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        onNavIconPressed = onNavIconPressed,
        title = {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                ProfileIcon(
                    size = 40.dp,  // Slightly larger profile icon
                    name = fullname,
                    modifier = Modifier.padding(start = 12.dp)
                )
                Spacer(
                    modifier = Modifier.width(16.dp)
                )


            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                // Channel name
                Text(
                    text = fullname,
                    style = MaterialTheme.typography.titleMedium,
                )
                // Number of members
                Text(
                    text = "@$nickname",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }}

        },
        actions = {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null
                )
            }
        }
    )
}


@Composable
private fun Messages(
    messages: List<ChatMessage>,
    scrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    Box(
        modifier
    ){
    LazyColumn(
        reverseLayout = true,
        modifier = modifier,
        state = scrollState
    ) {
        items(messages) { message ->
            Text(
                text = message.content,
                modifier = Modifier.padding(16.dp),
            )
        }

    }
    val jumpThreshold = with(LocalDensity.current) {
        JumpToBottomThreshold.toPx()
    }

    // Show the button if the first visible item is not the first one or if the offset is
    // greater than the threshold.
    val jumpToBottomButtonEnabled by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex != 0 ||
                    scrollState.firstVisibleItemScrollOffset > jumpThreshold
        }
    }

    JumpToBottom(
        // Only show if the scroller is not at the bottom
        enabled = jumpToBottomButtonEnabled,
        onClicked = {
            scope.launch {
                scrollState.animateScrollToItem(0)
            }
        },
        modifier = Modifier.align(Alignment.BottomCenter),
    )}
}

private val JumpToBottomThreshold = 56.dp

fun generateSampleMessages(): List<ChatMessage> {
    val messages = mutableListOf<ChatMessage>()
    val chatId = "chat1"
    val user1 = "user1"
    val user2 = "user2"
    val now = Date()

    repeat(25) { index ->
        val isUser1Sender = index % 2 == 0
        val senderId = if (isUser1Sender) user1 else user2
        val recipientId = if (isUser1Sender) user2 else user1
        val content = if (isUser1Sender) "Hello from $senderId" else "Reply from $senderId"

        messages.add(
            ChatMessage(
                id = UUID.randomUUID().toString(),
                chatId = chatId,
                senderId = senderId,
                recipientId = recipientId,
                content = "$content - message ${index + 1}",
                timestamp = Date(now.time + index * 60 * 1000L) // each message 1 minute apart
            )
        )
    }

    return messages
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatRoomAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavIconPressed: () -> Unit = { },
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        actions = actions,
        title = title,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(
                onClick = onNavIconPressed
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )}
        },
    )
}
