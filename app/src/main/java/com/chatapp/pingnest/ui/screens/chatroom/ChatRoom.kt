@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)

package com.chatapp.pingnest.ui.screens.chatroom

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chatapp.pingnest.data.models.ChatMessage
import com.chatapp.pingnest.data.models.ChatThemeType
import com.chatapp.pingnest.data.models.ChatWallpaperType
import com.chatapp.pingnest.data.models.Status
import com.chatapp.pingnest.data.models.User
import com.chatapp.pingnest.ui.components.FunctionalityNotAvailablePopup
import com.chatapp.pingnest.ui.components.JumpToBottom
import com.chatapp.pingnest.ui.components.InputBar
import com.chatapp.pingnest.ui.components.ProfileIcon
import com.chatapp.pingnest.ui.wallpapers.CircleWallpaper
import com.chatapp.pingnest.ui.wallpapers.CurveWallpaper
import com.chatapp.pingnest.ui.wallpapers.DoodleBackground
import com.chatapp.pingnest.ui.wallpapers.PolygonWallpaper
import com.chatapp.pingnest.ui.wallpapers.StarWallpaper
import com.chatapp.pingnest.ui.conversation.SymbolAnnotationType
import com.chatapp.pingnest.ui.wallpapers.WaveWallpaper
import com.chatapp.pingnest.ui.conversation.messageFormatter
import com.chatapp.pingnest.ui.screens.settings.toChatTheme
import com.chatapp.pingnest.ui.theme.PingNestTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Preview
@Composable
private fun ChatRoomPrev() {
    var text by remember {  mutableStateOf("")}
    PingNestTheme { 
        ChatRoom(
            modifier = Modifier,
            user = User(nickName = "testuser" , fullName = "Test User", status = Status.ONLINE),
            sender = "testuser",
            messages = listOf(
                ChatMessage(senderId = "testuser", text = "Hello!", recipientId = "otheruser", timestamp = "2024-03-15 10:00:00"),
                ChatMessage(senderId = "otheruser", text = "Hi there!",recipientId = "otheruser", timestamp = "2024-03-15 10:01:00"),
                ChatMessage(senderId = "testuser", text = "How are you doing?",recipientId = "otheruser", timestamp = "2024-03-15 10:02:00"),
                ChatMessage(senderId = "otheruser", text = "I'm good, thanks! How about you?", recipientId = "otheruser",timestamp = "2024-03-15 10:03:00"),
                ChatMessage(senderId = "testuser", text = "Doing well. Just working on this chat app.",recipientId = "otheruser", timestamp = "2024-03-15 10:04:00"),
                ChatMessage(senderId = "otheruser", text = "Oh cool! Sounds interesting.",recipientId = "otheruser", timestamp = "2024-03-15 10:05:00"),
                ChatMessage(senderId = "testuser", text = "Yeah, it's a fun project.", recipientId = "otheruser",timestamp = "2024-03-16 11:00:00"),
                ChatMessage(senderId = "otheruser", text = "I can imagine. Keep up the good work!",recipientId = "otheruser", timestamp = "2024-03-16 11:01:00"),
                ChatMessage(senderId = "testuser", text = "Thanks! Will do. https://www.google.com",recipientId = "otheruser", timestamp = "2024-03-16 11:02:00"),
                ChatMessage(senderId = "otheruser", text = "Let me know if you need any help @testuser",recipientId = "otheruser", timestamp = "2024-03-16 11:03:00")
            ),
            onNavIconPressed = {},
            onSend = { },
            onMessageChange = { text = it},
            onPhotoPickerClicked = {},
            onCameraClicked = {},
            onVideoClicked = {}
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
    onMessageChange: (String) -> Unit,
    onPhotoPickerClicked: ()-> Unit,
    onVideoClicked: (String) -> Unit,
    onCameraClicked:()-> Unit,
    viewModel: ChatRoomViewModel = koinViewModel()
) {
    val chatThemeType by viewModel.chatThemeFlow.collectAsStateWithLifecycle(initialValue = ChatThemeType.EMERALD_GREEN)
    val colorScheme = MaterialTheme.colorScheme
    val themeColors = chatThemeType.toChatTheme(colorScheme)
    val chatWallpaperType by viewModel.wallpaperFlow.collectAsStateWithLifecycle(ChatWallpaperType.DEFAULT)
    var popUp by remember { mutableStateOf(false) }
    if (user != null) {
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
                    modifier = Modifier,
                    onSendMessage = {
                        onMessageChange(it.text)
                        onSend()
                    },
                    onCameraClick = onCameraClicked,
                    onGalleryClick = onPhotoPickerClicked,
                    primaryColor = themeColors.chatBubbleColorLocalUser
                )
            }

        ) { innerPadding ->
            Box(
                Modifier.fillMaxSize().padding(innerPadding)
                    .background(themeColors.background)
            ) {
                if (popUp){
                    FunctionalityNotAvailablePopup(onDismiss = {popUp = !popUp})
                }
                when (chatWallpaperType) {
                    ChatWallpaperType.DEFAULT -> DoodleBackground()
                    ChatWallpaperType.CIRCLE -> CircleWallpaper()
                    ChatWallpaperType.STAR -> StarWallpaper()
                    ChatWallpaperType.CURVES -> CurveWallpaper()
                    ChatWallpaperType.WAVE -> WaveWallpaper()
                    ChatWallpaperType.POLYGON -> PolygonWallpaper()
                }
                Column(
                    modifier = modifier
                        .fillMaxSize()

                ) {

                    Messages(
                        messages = messages.reversed(),
                        scrollState = rememberLazyListState(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        sender = sender,
                        senderBubble = themeColors.chatBubbleColorLocalUser,
                        recipientBubble = themeColors.chatBubbleColorRemoteUser,
                        onVideoClicked = onVideoClicked
                    )


                }


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
private fun Messages(
    messages: List<ChatMessage>,
    scrollState: LazyListState,
    modifier: Modifier = Modifier,
    sender: String,
    senderBubble: Color,
    recipientBubble: Color,
    onVideoClicked: (String) -> Unit
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
                        authorClicked = {},
                        senderBubble = senderBubble,
                        recipientBubble = recipientBubble,
                        onVideoClicked = onVideoClicked
                    )

                }
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 2.dp, horizontal = 8.dp),
//                        horizontalArrangement = if (isUserMe) Arrangement.End else Arrangement.Start
//                    ) {
//
//                            Text(
//                                text = message.timestamp.toFormattedTime(),
//                                style = MaterialTheme.typography.bodySmall,
//                                color = LocalContentColor.current,
//                                fontWeight = FontWeight.ExtraBold
//                            )
//
//                    }




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


@Composable
private fun ChatItemBubble(
    message: ChatMessage,
    senderBubble: Color,
    recipientBubble: Color,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit,
    onVideoClicked: (String) -> Unit) {

    val backgroundBubbleColor = if (isUserMe) {
        senderBubble
    } else {
        recipientBubble
    }

    val chatBubbleShape = if (isUserMe){
        RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
    } else {
        RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
    }



    Card(
        elevation = CardDefaults.cardElevation(
//            defaultElevation = 5.dp
        ),
        modifier = Modifier,
//            .shadow(10.dp,chatBubbleShape),
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
                    onVideoClicked = onVideoClicked
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
private fun ClickableMessage(
    message: ChatMessage,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit,
    onVideoClicked: (uri: String)-> Unit
) {
    val uriHandler = LocalUriHandler.current
    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val activity = LocalActivity.current
    val context = LocalContext.current
    val windowSize = activity?.let { calculateWindowSizeClass(it) }
    val styledMessage = messageFormatter(
        text = message.text,
        primary = isUserMe,
    )
    val messageBubbleWidth = if (windowSize?.widthSizeClass == WindowWidthSizeClass.Compact) 240.dp else 480.dp

    Column {

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
                                        SymbolAnnotationType.LINK.name -> uriHandler.openUri(
                                            annotation.item
                                        )

                                        SymbolAnnotationType.PERSON.name -> authorClicked(annotation.item)
                                    }
                                }
                        }
                    }
                },
            onTextLayout = { layoutResult = it },
            style = MaterialTheme.typography.bodyLarge.copy(
                color = LocalContentColor.current,
                fontSize = 16.sp
            ),
            maxLines = Int.MAX_VALUE,
            overflow = TextOverflow.Clip
        )

        if(message.mediaUri != null){
            val mimeType = message.mediaMimeType
            if (mimeType != null){
                if (mimeType.contains("image")){
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(message.mediaUri)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .height(250.dp)
                            .padding(10.dp),
                    )
                } else if (mimeType.contains("video")){
                        VideoMessagePreview(
                            videoUri = message.mediaUri,
                            onClick = { onVideoClicked(message.mediaUri) }
                        )
                } else {
                    Box(modifier = Modifier.height(250.dp)){
                        Text("Unsupported media Type")
                    }
                }
            } else {
                Box(modifier = Modifier.height(250.dp)){
                    Text("No mimeType associated")
                }
            }
        }

    }
}

@Composable
private fun VideoMessagePreview(videoUri: String, onClick: () -> Unit) {
    val context = LocalContext.current.applicationContext

    // Running on an IO thread for loading metadata from remote urls to reduce lag time
    val bitmapState = produceState<Bitmap?>(initialValue = null) {
        withContext(Dispatchers.IO) {
            val mediaMetadataRetriever = MediaMetadataRetriever()

            // Remote url
            if (videoUri.contains("https://")) {
                mediaMetadataRetriever.setDataSource(videoUri, HashMap<String, String>())
            } else { // Locally saved files
                mediaMetadataRetriever.setDataSource(context, Uri.parse(videoUri))
            }
            // Return any frame that the framework considers representative of a valid frame
            value = mediaMetadataRetriever.frameAtTime
        }
    }

    bitmapState.value?.let { bitmap ->
        Box(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(10.dp),
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Gray, BlendMode.Darken),
            )

            Icon(
                Icons.Filled.PlayArrow,
                tint = Color.White,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center)
                    .border(3.dp, Color.White, shape = CircleShape),
            )
        }
    }
}

@Composable
private fun DayHeader(dayString: String) {
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
            color = MaterialTheme.colorScheme.inverseSurface,
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