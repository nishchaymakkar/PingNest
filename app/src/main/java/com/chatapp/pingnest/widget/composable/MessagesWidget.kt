package com.chatapp.pingnest.widget.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.text.Text
import com.chatapp.pingnest.PingNestActivity
import com.chatapp.pingnest.R
import com.chatapp.pingnest.data.models.ChatMessage
import com.chatapp.pingnest.widget.theme.PingNestGlanceColorScheme

@Composable
fun MessagesWidget(messages: List<ChatMessage>) {
    Scaffold(titleBar = {
        TitleBar(
            startIcon = ImageProvider(R.drawable.ic_launcher_foreground),
            iconColor = null,
            title = LocalContext.current.getString(R.string.messages_widget_title),
        )
    }, backgroundColor = PingNestGlanceColorScheme.colors.background) {
        LazyColumn(modifier = GlanceModifier.fillMaxWidth()) {
            messages.forEach {
                item {
                    Column(modifier = GlanceModifier.fillMaxWidth()) {
                        MessageItem(it)
                        Spacer(modifier = GlanceModifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun MessageItem(message: ChatMessage) {
    Column(modifier = GlanceModifier.clickable(actionStartActivity<PingNestActivity>()).fillMaxWidth()) {
        Text(
            text = message.senderId
        )
        Text(
            text = message.content
        )
    }
}



@Preview
@Composable
fun WidgetPreview() {
    MessagesWidget(listOf(ChatMessage(senderId = "John", content =  "This is a preview of the message Item", timestamp =  "8:02PM", recipientId = "")))
}
