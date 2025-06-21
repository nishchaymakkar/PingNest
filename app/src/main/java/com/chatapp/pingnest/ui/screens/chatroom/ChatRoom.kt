@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)

package com.chatapp.pingnest.ui.screens.chatroom

import android.annotation.SuppressLint
import androidx.activity.compose.LocalActivity
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chatapp.pingnest.data.models.ChatMessage
import com.chatapp.pingnest.data.models.Status
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.ui.components.JumpToBottom
import com.chatapp.pingnest.ui.components.InputBar
import com.chatapp.pingnest.ui.components.ProfileIcon
import com.chatapp.pingnest.ui.conversation.SymbolAnnotationType
import com.chatapp.pingnest.ui.conversation.messageFormatter
import com.chatapp.pingnest.ui.theme.PingNestTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Preview( showSystemUi = true)
@Composable
private fun ChatRoomPrev() {
    var text by remember {  mutableStateOf("")}
    PingNestTheme { 
        ChatRoom(
            modifier = Modifier,
            user = User(nickName = "testuser" , fullName = "Test User", status = Status.ONLINE),
            sender = "testuser",
            messages = listOf(
                ChatMessage(senderId = "testuser", content = "Hello!", recipientId = "otheruser", timestamp = "2024-03-15 10:00:00"),
                ChatMessage(senderId = "otheruser", content = "Hi there!",recipientId = "otheruser", timestamp = "2024-03-15 10:01:00"),
                ChatMessage(senderId = "testuser", content = "How are you doing?",recipientId = "otheruser", timestamp = "2024-03-15 10:02:00"),
                ChatMessage(senderId = "otheruser", content = "I'm good, thanks! How about you?", recipientId = "otheruser",timestamp = "2024-03-15 10:03:00"),
                ChatMessage(senderId = "testuser", content = "Doing well. Just working on this chat app.",recipientId = "otheruser", timestamp = "2024-03-15 10:04:00"),
                ChatMessage(senderId = "otheruser", content = "Oh cool! Sounds interesting.",recipientId = "otheruser", timestamp = "2024-03-15 10:05:00"),
                ChatMessage(senderId = "testuser", content = "Yeah, it's a fun project.", recipientId = "otheruser",timestamp = "2024-03-16 11:00:00"),
                ChatMessage(senderId = "otheruser", content = "I can imagine. Keep up the good work!",recipientId = "otheruser", timestamp = "2024-03-16 11:01:00"),
                ChatMessage(senderId = "testuser", content = "Thanks! Will do. https://www.google.com",recipientId = "otheruser", timestamp = "2024-03-16 11:02:00"),
                ChatMessage(senderId = "otheruser", content = "Let me know if you need any help @testuser",recipientId = "otheruser", timestamp = "2024-03-16 11:03:00")
            ),
            onNavIconPressed = {},
            onSend = { },
            onMessageChange = { text = it}
        )
    }
}


@Composable
fun ChatRoom(
    modifier: Modifier = Modifier,
    user: User?,
    sender: String,
    messages: List<ChatMessage>,
    onNavIconPressed: () -> Unit,
    onSend: ()-> Unit,
    onMessageChange: (String) -> Unit
) {


    if(user != null) {
        Scaffold(
            topBar = {
                ChatNameBar(
                    fullname = user.fullName,
                    nickname = user.nickName,
                    modifier = Modifier,
                    onNavIconPressed = onNavIconPressed,
                )
            },
            bottomBar = {
                InputBar(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    onSendMessage = {
                        onMessageChange(it.text)
                        onSend() },
                    onCameraClick = {},
                    onGalleryClick = {}
                )
            }

        ) { innerPadding ->

                Column(
                    modifier = modifier .fillMaxSize()
                        .padding(innerPadding).background(MaterialTheme.colorScheme.background)
                ) {

                    Messages(
                        messages = messages.reversed(),
                        scrollState = rememberLazyListState(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        sender = sender
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
                    size = 40.dp,
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
    modifier: Modifier = Modifier,
    sender: String,
) {
    val scope = rememberCoroutineScope()

    Box(
        modifier.fillMaxSize()
    ) {
        LazyColumn(
            reverseLayout = true,
            modifier = modifier,
            state = scrollState,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            itemsIndexed(messages) { index, message ->
                val isUserMe = message.senderId == sender

                val currentDate = message.timestamp.toLocalDate()
                val previousDate = messages.getOrNull(index + 1)?.timestamp?.toLocalDate()

                if (currentDate != null && currentDate != previousDate) {
                    DayHeader(dayString = currentDate.toDisplayString())
                }
                Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 8.dp),
                    horizontalArrangement = if (isUserMe) Arrangement.End else Arrangement.Start
                ) {

                    ChatItemBubble(
                        message = message,
                        isUserMe = isUserMe,
                        authorClicked = {}
                    )

                }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp, horizontal = 8.dp),
                        horizontalArrangement = if (isUserMe) Arrangement.End else Arrangement.Start
                    ) {

                            Text(
                                text = message.timestamp.toFormattedTime(),
                                style = MaterialTheme.typography.bodySmall,
                                color = LocalContentColor.current,
                                fontWeight = FontWeight.ExtraBold
                            )

                    }




                }

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
        )
    }
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
        MaterialTheme.colorScheme.surfaceContainer
    }

    val chatBubbleShape = if (isUserMe){
        RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
    } else {
        RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
    }



    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        modifier = Modifier.shadow(10.dp,chatBubbleShape),
        shape = chatBubbleShape
    ) {
        Surface(
            color = backgroundBubbleColor,
            shape = chatBubbleShape,
        ) {
            Column {

                ClickableMessage(
                    message = message,
                    isUserMe = isUserMe,
                    authorClicked = authorClicked,
                )


            }
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

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ClickableMessage( message: ChatMessage, isUserMe: Boolean, authorClicked: (String) -> Unit) {
    val uriHandler = LocalUriHandler.current
    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val activity = LocalActivity.current
    val windowSize = activity?.let { calculateWindowSizeClass(it) }
    val styledMessage = messageFormatter(
        text = message.content,
        primary = isUserMe,
    )
    val messageBubbleWidth = if (windowSize?.widthSizeClass == WindowWidthSizeClass.Compact) 240.dp else 480.dp


    BasicText(
        text = styledMessage,
        modifier = Modifier
            .padding(16.dp)
            .widthIn(max = messageBubbleWidth)
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



@Composable
fun DayHeader(dayString: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .height(16.dp),
    ) {
        DayHeaderLine()
        Text(
            text = dayString,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        DayHeaderLine()
    }
}

@Composable
private fun RowScope.DayHeaderLine() {
    HorizontalDivider(
        modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterVertically),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
    )
}
@SuppressLint("NewApi")
fun String.toLocalDate(): LocalDate? {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        LocalDateTime.parse(this, formatter).toLocalDate()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


@SuppressLint("NewApi")
fun LocalDate.toDisplayString(): String {
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
    return this.format(formatter)
}
@SuppressLint("NewApi")
fun String.toFormattedTime(): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(this, formatter)
        dateTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
    } catch (e: Exception) {
        this
    }
}