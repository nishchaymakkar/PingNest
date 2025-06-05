@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)

package com.chatapp.pingnest.ui.screens.chatroom

import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
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
import androidx.compose.material3.windowsizeclass.WindowSizeClass
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
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
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale
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
        },
        bottomBar = {
            MessageTextField(
                modifier = Modifier.background(Color.Transparent)
                    .padding(bottom = 8.dp),
                text = "Nishchay Makkar",
                onTextChange = {}
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
    ) {
        LazyColumn(
            reverseLayout = true,
            modifier = modifier,
            state = scrollState,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            itemsIndexed(messages) { index, message ->
                val isUserMe = message.senderId == "user1"

                val currentDay = message.timestamp.toLocalDateString()
                val previousDay = messages.getOrNull(index + 1)?.timestamp?.toLocalDateString()

                if (currentDay != previousDay) {

                    DayHeader(dayString = currentDay)

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
                            text = message.timestamp.toLocalDateStringForMessage(),
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
        MaterialTheme.colorScheme.surfaceVariant
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


val chatMessages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    listOf(
        ChatMessage("1", "chat1", "user1", "user2", "Hey, how's your day going?", localDateToDate(2025, 6, 2, 9, 0)),
        ChatMessage("2", "chat1", "user2", "user1", "Hey! It's going alright, just been juggling some tasks at work. How about you?", localDateToDate(2025, 6, 3, 10, 15)),
        ChatMessage("3", "chat1", "user1", "user2", "Pretty hectic morning honestly. Spent almost 3 hours debugging an issue caused by a missing semicolon 🤦‍♂️", localDateToDate(2025, 6, 3, 10, 45)),
        ChatMessage("4", "chat1", "user2", "user1", "Oh no! Classic dev problem. Been there, done that. At least you found it!", localDateToDate(2025, 6, 4, 8, 30)),
        ChatMessage("5", "chat1", "user1", "user2", "True. I just needed to walk away for 10 minutes and the solution magically appeared 😂", localDateToDate(2025, 6, 4, 8, 50)),
        ChatMessage("6", "chat1", "user2", "user1", "Honestly, that’s my favorite debugging technique. Step away and let the brain do its thing.", localDateToDate(2025, 6, 4, 9, 5)),
        ChatMessage("7", "chat1", "user1", "user2", "By the way, did you get a chance to review the design doc I shared yesterday?", localDateToDate(2025, 6, 5, 9, 6)),
        ChatMessage("8", "chat1", "user2", "user1", "Not yet. I was meaning to do it last night but got caught up with some chores. Will do it by lunch today.", localDateToDate(2025, 6, 5, 9, 30)),
        ChatMessage("9", "chat1", "user1", "user2", "No worries, just wanted to make sure it didn’t slip through. We’re hoping to freeze it by EOD.", localDateToDate(2025, 6, 5, 9, 35)),
        ChatMessage("10", "chat1", "user2", "user1", "Got it. I’ll prioritize it first thing after this meeting I have at 10.", localDateToDate(2025, 6, 5, 9, 50)),
        ChatMessage("11", "chat1", "user1", "user2", "Thanks! Oh and random — did you see the new Pixel 9 leaks? They look 🔥", localDateToDate(2025, 6, 5, 10, 10)),
        ChatMessage("12", "chat1", "user2", "user1", "Yessss! That matte black finish is everything. Also, finally an in-display fingerprint that works?", localDateToDate(2025, 6, 5, 10, 20)),
        ChatMessage("13", "chat1", "user1", "user2", "If it lives up to the hype, I might actually switch from Samsung. Been a while since Google impressed me tbh.", localDateToDate(2025, 6, 5, 10, 25)),
        ChatMessage("14", "chat1", "user2", "user1", "Same! I just hope the battery life holds up. All the features are cool but mean nothing if you’re always charging.", localDateToDate(2025, 6, 5, 10, 35)),
        ChatMessage("15", "chat1", "user1", "user2", "Totally agree. Power banks are not the solution to poor design.", localDateToDate(2025, 6, 5, 10, 45)),
        ChatMessage("16", "chat1", "user2", "user1", "Speaking of design, that new chat UI you’re building looks super clean. I saw the Figma preview on Slack.", localDateToDate(2025, 6, 5, 11, 0)),
        ChatMessage("17", "chat1", "user1", "user2", "Thanks man! Been experimenting with Jetpack Compose and material3. The canonical layouts help a lot.", localDateToDate(2025, 6, 5, 11, 10)),
        ChatMessage("18", "chat1", "user2", "user1", "That’s awesome. I’ve been meaning to get into Compose more seriously. Been stuck with XML layouts forever 😅", localDateToDate(2025, 6, 5, 11, 25)),
        ChatMessage("19", "chat1", "user1", "user2", "Start with something small. Once you understand state hoisting and remember a few Composables, it feels like magic.", localDateToDate(2025, 6, 5, 11, 40)),
        ChatMessage("20", "chat1", "user2", "user1", "Noted! Maybe I’ll start with our internal tools. Less pressure there.", localDateToDate(2025, 6, 5, 12, 0)),
        ChatMessage("21", "chat1", "user1", "user2", "Great idea. Also if you get stuck, just ping me. Happy to help any time 😊", localDateToDate(2025, 6, 5, 12, 10)),
        ChatMessage("22", "chat1", "user2", "user1", "Really appreciate that. You’re like the Android guru in the team 😂", localDateToDate(2025, 6, 5, 12, 15)),
        ChatMessage("23", "chat1", "user1", "user2", "Haha, just obsessed with making cool UIs tbh. Still a long way to go!", localDateToDate(2025, 6, 5, 12, 25)),
        ChatMessage("24", "chat1", "user2", "user1", "Well, you’re doing great. Btw, wanna grab coffee around 4?", localDateToDate(2025, 6, 5, 15, 50)),
        ChatMessage("25", "chat1", "user1", "user2", "Absolutely. Let’s go to Brew Lab. Their iced latte is perfect for this weather.", localDateToDate(2025, 6, 5, 16, 30))
    )
} else {
    TODO("VERSION.SDK_INT < O")
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
fun Date.toLocalDateString(): String {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return formatter.format(this)
}
fun Date.toLocalDateStringForMessage(): String {
    val formatter = SimpleDateFormat("hh:mm", Locale.getDefault())
    return formatter.format(this)
}

@RequiresApi(Build.VERSION_CODES.O)
fun localDateToDate(year: Int, month: Int, day: Int, hour: Int = 0, minute: Int = 0): Date {
    return LocalDateTime.of(year, month, day, hour, minute)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .let { Date.from(it) }
}
