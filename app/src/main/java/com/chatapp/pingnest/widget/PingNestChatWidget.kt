package com.chatapp.pingnest.widget

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme

import androidx.glance.text.Text
import androidx.glance.layout.*
import androidx.glance.appwidget.*
import androidx.glance.*
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import com.chatapp.pingnest.data.models.ChatMessage
import com.chatapp.pingnest.widget.composable.MessagesWidget
import com.chatapp.pingnest.widget.theme.PingNestGlanceColorScheme

val unreadMessages = listOf(
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
)
class PingNestChatWidget: GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {
            GlanceTheme {

                Column(
                    modifier = GlanceModifier.fillMaxHeight().fillMaxWidth().background(PingNestGlanceColorScheme.colors.surface),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MessagesWidget(messages = unreadMessages)
                }
            }
        }
    }
}