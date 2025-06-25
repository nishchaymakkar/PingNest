@file:OptIn(ExperimentalMaterial3Api::class)

package com.chatapp.pingnest.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chatapp.pingnest.data.models.AppTheme
import com.chatapp.pingnest.data.models.ChatTheme
import com.chatapp.pingnest.data.models.ChatThemeType
import com.chatapp.pingnest.data.models.ChatWallpaperType
import com.chatapp.pingnest.ui.wallpapers.CircleWallpaper
import com.chatapp.pingnest.ui.wallpapers.CurveWallpaper
import com.chatapp.pingnest.ui.wallpapers.DoodleBackground
import com.chatapp.pingnest.ui.wallpapers.PolygonWallpaper
import com.chatapp.pingnest.ui.wallpapers.StarWallpaper
import com.chatapp.pingnest.ui.wallpapers.WaveWallpaper
import com.chatapp.pingnest.ui.theme.PingNestTheme

@Preview
@Composable
private fun ChatThemePreview() {
    PingNestTheme(darkTheme = false) {
        ChatThemeSettings(
            onNavBackClicked = {},
            onThemeChange = {},
            appTheme = AppTheme.SYSTEM_DEFAULT,
            onChatThemeChange = {},
            defaultChatTheme = ChatThemeType.EMERALD_GREEN,
            defaultWallpaper = ChatWallpaperType.DEFAULT,
            selectedWallpaper = {}
        )
    }
}
@Composable
fun ChatThemeSettings(
    modifier: Modifier = Modifier,
    onNavBackClicked: () -> Unit,
    onThemeChange: (AppTheme) -> Unit,
    appTheme: AppTheme,
    defaultChatTheme: ChatThemeType,
    onChatThemeChange: (ChatThemeType) -> Unit,
    defaultWallpaper: ChatWallpaperType,
    selectedWallpaper: (ChatWallpaperType) -> Unit
) {


        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SettingBar(
                text = "Chat Theme",
                onNavBackClicked = onNavBackClicked,
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Box(
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            Column(Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {
                ItemHeader(
                    text = "Default Theme",
                    modifier = Modifier.padding(top = 8.dp)
                )
                ThemeChooser(
                    savedAppTheme = appTheme,
                    onThemeChange = onThemeChange
                )
                HorizontalDivider()
                ItemHeader(
                    text = "Chat Theme",
                    modifier = Modifier.padding(top = 8.dp)
                )
                ChatThemeSelector(
                    chatThemes = ChatThemeType.entries,
                    defaultTheme = defaultChatTheme,
                   onThemeSelected = onChatThemeChange
                )
                HorizontalDivider()
                ItemHeader(
                    text = "Chat Wallpaper",
                    modifier = Modifier.padding(top = 8.dp)
                )
                ChatWallPaperSelector(
                    defaultWallpaper = defaultWallpaper,
                    chatWallpapers = ChatWallpaperType.entries,
                    selectedWallpaper = selectedWallpaper,
                )

            }
        }
    }

}

@Composable
fun ItemHeader(
    modifier: Modifier = Modifier,
    text : String
) {
    Row (
        modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){
       Text(
            text = text,
           fontWeight = FontWeight.SemiBold,
           style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
private fun ThemeChooser(
    savedAppTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
    modifier: Modifier = Modifier) {
    Column(modifier.padding(
        horizontal = 16.dp,
        vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start) {
        for (i in 0..2) {
            val appTheme = AppTheme.entries[i]

            Row(
                modifier.clickable(onClick = {onThemeChange(appTheme)}), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                RadioButton(
                    onClick = { onThemeChange(appTheme) },
                    selected = appTheme == savedAppTheme,
                )
                Text(
                    text = appTheme.toString().toSentenceCase()
                    .replace('_',' '),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium

                )
            }
        }

    }

}

fun String.toSentenceCase(): String {
    val trimmed = this.trim()
    if (trimmed.isEmpty()) return trimmed
    return trimmed.lowercase().replaceFirstChar { it.uppercase() }
}

@Preview
@Composable
private fun ChatThemeOptionPrev() {
    PingNestTheme {
        ChatThemeOption(
            chatTheme = ChatThemeType.EMERALD_GREEN
        )

    }
    
}


@Composable
private fun ChatThemeSelector(
    defaultTheme: ChatThemeType,
    chatThemes: List<ChatThemeType>,
    onThemeSelected: (ChatThemeType) -> Unit
) {

    LazyRow(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(chatThemes) { index, chatTheme ->
            val isSelected = defaultTheme == chatTheme

            Box(
                modifier = Modifier
                    .border(
                        width = 4.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = RoundedCornerShape(15.dp)
                    )
            ) {
                ChatThemeOption(
                    chatTheme = chatTheme,
                    modifier = Modifier
                        .clickable {
                            onThemeSelected(chatTheme)
                        }
                )

                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
@Composable
private fun ChatThemeOption(chatTheme: ChatThemeType, modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    val themeColors = chatTheme.toChatTheme(colorScheme)
    Box(modifier = modifier
        .size(
            width = 125.dp,
            height = 150.dp
        )
        .clip(RoundedCornerShape(15.dp))
        .background(themeColors.background)
    ){
        Column(
            Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                ChatBubble(
                    isUser = false,
                    color = themeColors.chatBubbleColorRemoteUser
                )
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                ChatBubble(
                    isUser = true,
                    color = themeColors.chatBubbleColorLocalUser
                )
            }
        }
    }
    
}

@Composable
private fun ChatWallpaperOption(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier
        .size(
            width = 125.dp,
            height = 150.dp
        )
        .clip(RoundedCornerShape(15.dp)),
        content = content,
        contentAlignment = Alignment.Center
    )
}


@Composable
private fun ChatWallPaperSelector(
    defaultWallpaper: ChatWallpaperType,
  chatWallpapers: List<ChatWallpaperType>,
    selectedWallpaper: (ChatWallpaperType) -> Unit) {
    LazyRow(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(chatWallpapers) { index, chatWallpaper ->
            val isSelected = defaultWallpaper == chatWallpaper
            val wallpaperContent: @Composable BoxScope.() -> Unit = chatWallpaper.toChatWallpaper()

            Box(
                modifier = Modifier
                    .border(
                        width = 4.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = RoundedCornerShape(15.dp)
                    )
            ) {
               ChatWallpaperOption(
                   content = wallpaperContent,
                   modifier = Modifier.clickable{
                       selectedWallpaper(chatWallpaper)
                   }
               )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }

}

@Composable
private fun ChatBubble(modifier: Modifier = Modifier,isUser: Boolean,color: Color) {
    val bubbleShape = if(isUser){
        RoundedCornerShape(
            topStart = 15.dp,
            topEnd = 0.dp,
            bottomStart = 15.dp,
            bottomEnd = 15.dp)
    } else {
        RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 15.dp,
            bottomStart = 15.dp,
            bottomEnd = 15.dp
        )
    }
    Card(shape = bubbleShape, modifier = Modifier
        .height(15.dp)
        .width(40.dp)) {
        Box(modifier
            .fillMaxSize()
            .background(color))
    }
}

fun ChatThemeType.toChatTheme(colors: ColorScheme): ChatTheme = when (this) {
    ChatThemeType.EMERALD_GREEN -> ChatTheme(
        background = colors.background,
        chatBubbleColorLocalUser = colors.primary,
        chatBubbleColorRemoteUser = colors.surfaceVariant
    )
    ChatThemeType.NEON_MINT -> ChatTheme(
        background = colors.primaryContainer.copy(alpha = 0.2f),
        chatBubbleColorLocalUser = colors.primaryContainer,
        chatBubbleColorRemoteUser = colors.surfaceVariant
    )
    ChatThemeType.GOLDEN_GLOW -> ChatTheme(
        background = colors.secondaryContainer.copy(alpha = 0.2f),
        chatBubbleColorLocalUser = colors.secondaryContainer,
        chatBubbleColorRemoteUser = colors.surfaceVariant
    )
    ChatThemeType.AMBER_BROWN -> ChatTheme(
        background = colors.secondary.copy(alpha = 0.2f),
        chatBubbleColorLocalUser = colors.secondary,
        chatBubbleColorRemoteUser = colors.surfaceVariant
    )
    ChatThemeType.CRIMSON_FLAME -> ChatTheme(
        background = colors.tertiary.copy(alpha = 0.2f),
        chatBubbleColorLocalUser = colors.tertiary,
        chatBubbleColorRemoteUser = colors.surfaceVariant
    )
    ChatThemeType.ROSE_BLUSH -> ChatTheme(
        background = colors.tertiaryContainer.copy(alpha = 0.2f),
        chatBubbleColorLocalUser = colors.tertiaryContainer,
        chatBubbleColorRemoteUser = colors.surfaceVariant
    )
}


fun ChatWallpaperType.toChatWallpaper(): @Composable BoxScope.() -> Unit {
    return when (this) {
        ChatWallpaperType.DEFAULT -> {
            { DoodleBackground() }
        }

        ChatWallpaperType.STAR -> {
            { StarWallpaper() }
        }

        ChatWallpaperType.CIRCLE -> {
            { CircleWallpaper() }
        }

        ChatWallpaperType.CURVES -> {
            { CurveWallpaper() }
        }

        ChatWallpaperType.POLYGON -> {
            { PolygonWallpaper() }
        }

        ChatWallpaperType.WAVE -> {
            { WaveWallpaper() }
        }
    }
}

