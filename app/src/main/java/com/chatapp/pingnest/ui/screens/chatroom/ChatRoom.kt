@file:OptIn(ExperimentalMaterial3Api::class)

package com.chatapp.pingnest.ui.screens.chatroom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chatapp.pingnest.data.models.ChatMessage
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.ui.components.JumpToBottom
import com.chatapp.pingnest.ui.components.MessageTextField
import com.chatapp.pingnest.ui.components.ProfileIcon
import com.chatapp.pingnest.ui.conversation.SymbolAnnotationType
import com.chatapp.pingnest.ui.conversation.messageFormatter
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
                messages = chatMessages.reversed(),
                scrollState = rememberLazyListState(),
                modifier = Modifier.fillMaxWidth().weight(1f)
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
        state = scrollState,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        items(messages) { message ->
            val isUserMe = if (message.senderId == "user1") true else false

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                horizontalArrangement = if (isUserMe) Arrangement.End else Arrangement.Start
            ){
            ChatItemBubble(
                message = message,
                isUserMe = isUserMe,
                authorClicked = {},
                           )
        }}

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

@Composable
fun ChatItemBubble(message: ChatMessage, isUserMe: Boolean, authorClicked: (String) -> Unit) {

    val backgroundBubbleColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val chatBubbleShape = if (isUserMe){
        RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
    } else {
        RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
    }



    Column {
        Surface(
            color = backgroundBubbleColor,
            shape = chatBubbleShape,
        ) {
            ClickableMessage(
                message = message,
                isUserMe = isUserMe,
                authorClicked = authorClicked,
            )
        }

//        message.image?.let {
//            Spacer(modifier = Modifier.height(4.dp))
//            Surface(
//                color = backgroundBubbleColor,
//                shape = ChatBubbleShape,
//            ) {
//                Image(
//                    painter = painterResource(it),
//                    contentScale = ContentScale.Fit,
//                    modifier = Modifier.size(160.dp),
//                    contentDescription = stringResource(id = R.string.attached_image),
//                )
//            }
//        }
    }
}
@Composable
fun ClickableMessage( message: ChatMessage, isUserMe: Boolean, authorClicked: (String) -> Unit) {
    val uriHandler = LocalUriHandler.current
    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val styledMessage = messageFormatter(
        text = message.content,
        primary = isUserMe,
    )

    BasicText(
        text = styledMessage,
        modifier = Modifier
            .padding(16.dp)
            .widthIn(max = 240.dp)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    layoutResult?.let { layout ->
                        val position = layout.getOffsetForPosition(offset)
                        styledMessage.getStringAnnotations(start = position, end = position)
                            .firstOrNull()?.let { annotation ->
                                when (annotation.tag) {
                                    SymbolAnnotationType.LINK.name -> uriHandler.openUri(annotation.item)
                                    SymbolAnnotationType.PERSON.name -> authorClicked(annotation.item)
                                }
                            }
                    }
                }
            },
        onTextLayout = {layoutResult = it},
        style = MaterialTheme.typography.bodyLarge.copy(
            color = LocalContentColor.current,
            fontSize = 16.sp
        ),
        maxLines = Int.MAX_VALUE,
        overflow = TextOverflow.Clip
    )
}


val chatMessages = listOf(
    ChatMessage("1", "chat1", "user1", "user2", "Hey, how's your day going?", Date(2025, 6, 5, 9, 0)),
    ChatMessage("2", "chat1", "user2", "user1", "Hey! It's going alright, just been juggling some tasks at work. How about you?", Date(2025, 6, 5, 9, 1)),
    ChatMessage("3", "chat1", "user1", "user2", "Pretty hectic morning honestly. Spent almost 3 hours debugging an issue caused by a missing semicolon 🤦‍♂️", Date(2025, 6, 5, 9, 2)),
    ChatMessage("4", "chat1", "user2", "user1", "Oh no! Classic dev problem. Been there, done that. At least you found it!", Date(2025, 6, 5, 9, 3)),
    ChatMessage("5", "chat1", "user1", "user2", "True. I just needed to walk away for 10 minutes and the solution magically appeared 😂", Date(2025, 6, 5, 9, 4)),
    ChatMessage("6", "chat1", "user2", "user1", "Honestly, that’s my favorite debugging technique. Step away and let the brain do its thing.", Date(2025, 6, 5, 9, 5)),
    ChatMessage("7", "chat1", "user1", "user2", "By the way, did you get a chance to review the design doc I shared yesterday?", Date(2025, 6, 5, 9, 6)),
    ChatMessage("8", "chat1", "user2", "user1", "Not yet. I was meaning to do it last night but got caught up with some chores. Will do it by lunch today.", Date(2025, 6, 5, 9, 7)),
    ChatMessage("9", "chat1", "user1", "user2", "No worries, just wanted to make sure it didn’t slip through. We’re hoping to freeze it by EOD.", Date(2025, 6, 5, 9, 8)),
    ChatMessage("10", "chat1", "user2", "user1", "Got it. I’ll prioritize it first thing after this meeting I have at 10.", Date(2025, 6, 5, 9, 9)),
    ChatMessage("11", "chat1", "user1", "user2", "Thanks! Oh and random — did you see the new Pixel 9 leaks? They look 🔥", Date(2025, 6, 5, 9, 10)),
    ChatMessage("12", "chat1", "user2", "user1", "Yessss! That matte black finish is everything. Also, finally an in-display fingerprint that works?", Date(2025, 6, 5, 9, 11)),
    ChatMessage("13", "chat1", "user1", "user2", "If it lives up to the hype, I might actually switch from Samsung. Been a while since Google impressed me tbh.", Date(2025, 6, 5, 9, 12)),
    ChatMessage("14", "chat1", "user2", "user1", "Same! I just hope the battery life holds up. All the features are cool but mean nothing if you’re always charging.", Date(2025, 6, 5, 9, 13)),
    ChatMessage("15", "chat1", "user1", "user2", "Totally agree. Power banks are not the solution to poor design.", Date(2025, 6, 5, 9, 14)),
    ChatMessage("16", "chat1", "user2", "user1", "Speaking of design, that new chat UI you’re building looks super clean. I saw the Figma preview on Slack.", Date(2025, 6, 5, 9, 15)),
    ChatMessage("17", "chat1", "user1", "user2", "Thanks man! Been experimenting with Jetpack Compose and material3. The canonical layouts help a lot.", Date(2025, 6, 5, 9, 16)),
    ChatMessage("18", "chat1", "user2", "user1", "That’s awesome. I’ve been meaning to get into Compose more seriously. Been stuck with XML layouts forever 😅", Date(2025, 6, 5, 9, 17)),
    ChatMessage("19", "chat1", "user1", "user2", "Start with something small. Once you understand state hoisting and remember a few Composables, it feels like magic.", Date(2025, 6, 5, 9, 18)),
    ChatMessage("20", "chat1", "user2", "user1", "Noted! Maybe I’ll start with our internal tools. Less pressure there.", Date(2025, 6, 5, 9, 19)),
    ChatMessage("21", "chat1", "user1", "user2", "Great idea. Also if you get stuck, just ping me. Happy to help any time 😊", Date(2025, 6, 5, 9, 20)),
    ChatMessage("22", "chat1", "user2", "user1", "Really appreciate that. You’re like the Android guru in the team 😂", Date(2025, 6, 5, 9, 21)),
    ChatMessage("23", "chat1", "user1", "user2", "Haha, just obsessed with making cool UIs tbh. Still a long way to go!", Date(2025, 6, 5, 9, 22)),
    ChatMessage("24", "chat1", "user2", "user1", "Well, you’re doing great. Btw, wanna grab coffee around 4?", Date(2025, 6, 5, 9, 23)),
    ChatMessage("25", "chat1", "user1", "user2", "Absolutely. Let’s go to Brew Lab. Their iced latte is perfect for this weather.", Date(2025, 6, 5, 9, 24))
)
