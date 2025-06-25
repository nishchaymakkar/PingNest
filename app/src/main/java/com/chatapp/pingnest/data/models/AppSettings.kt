package com.chatapp.pingnest.data.models

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val theme: AppTheme = AppTheme.SYSTEM_DEFAULT,
    val userData: User = User(nickName = "", fullName = "", status = Status.ONLINE),
    val chatThemeType: ChatThemeType = ChatThemeType.EMERALD_GREEN,
    val chatWallpaperType: ChatWallpaperType = ChatWallpaperType.DEFAULT
)
data class ChatTheme(
    val background: Color,
    val chatBubbleColorLocalUser: Color,
    val chatBubbleColorRemoteUser: Color
)

enum class AppTheme {
    LIGHT,
    DARK,
    SYSTEM_DEFAULT
}
enum class ChatThemeType {
    EMERALD_GREEN,
    NEON_MINT,
    GOLDEN_GLOW,
    AMBER_BROWN,
    CRIMSON_FLAME,
    ROSE_BLUSH
}

enum class ChatWallpaperType {
    DEFAULT,
    STAR,
    CIRCLE,
    CURVES,
    WAVE,
    POLYGON
}

