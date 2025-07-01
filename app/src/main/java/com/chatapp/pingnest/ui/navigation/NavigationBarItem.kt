package com.chatapp.pingnest.ui.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.FeaturedVideo
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.automirrored.outlined.FeaturedVideo
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.chatapp.pingnest.R
import com.chatapp.pingnest.ui.screens.homescreen.HomeNavGraph
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

val bottomBarItems = listOf<NavigationBarItem>(
    NavigationBarItem.ChatList,
    NavigationBarItem.TimeLine
)
@Serializable
sealed class NavigationBarItem(
    val title: String,
    val selectedIcon: Int,
    val unSelectedIcon: Int,
    val badgeCount: Int? = null,
    val hasNews: Boolean = false
) : NavKey {

    @Serializable
    data object ChatList: NavigationBarItem(
        title = "Chats",
        selectedIcon = R.drawable.chat_filled,
        unSelectedIcon = R.drawable.chat_outlined,
        hasNews = false
    )


    @Serializable
    data object TimeLine : NavigationBarItem(
        title = "Timeline",
        selectedIcon = R.drawable.timeline_filled,
        unSelectedIcon = R.drawable.timeline_outlined,
        hasNews = false
    )

}

val BottomBarScreenSaver = Saver<NavigationBarItem, String>(
    save = { it::class.java.name ?: "unknown"},
    restore = {
        when (it) {
            NavigationBarItem.ChatList::class.java.name -> NavigationBarItem.ChatList
            NavigationBarItem.TimeLine::class.java.name -> NavigationBarItem.TimeLine
            else -> NavigationBarItem.ChatList
        }

    }
)



