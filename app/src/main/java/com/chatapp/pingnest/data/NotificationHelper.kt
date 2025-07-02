package com.chatapp.pingnest.data

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Person
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import com.chatapp.pingnest.BubbleActivity
import com.chatapp.pingnest.CHANNEL_BUBBLE_ID
import com.chatapp.pingnest.NOTIFICATION_ID
import com.chatapp.pingnest.PingNestActivity
import com.chatapp.pingnest.R

class NotificationHelper(context: Context) {

    val appContext = context.applicationContext!!

    private fun flagUpdateCurrent(isMutable: Boolean): Int {
        return when {
            Build.VERSION.SDK_INT >= 31 && isMutable -> {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            }
            Build.VERSION.SDK_INT >= 31 && !isMutable -> {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            }
            else -> {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.N_MR1)
    val shortcutManager =
        context.getSystemService(ShortcutManager::class.java)!!

    val notificationManager = context.getSystemService(NotificationManager::class.java)!!


    val icon = Icon.createWithResource(context, R.drawable.bubble_chat)




    val shortcutIntent = Intent(context, PingNestActivity::class.java).apply {
        action = Intent.ACTION_VIEW
    }

    @WorkerThread
    @RequiresApi(Build.VERSION_CODES.Q)
    fun sendNotification(message: String, sender: String ) {
        val intent = Intent(appContext, BubbleActivity::class.java)
            .setAction(Intent.ACTION_VIEW).apply {
            putExtra("sender", sender)
        }
        val bubbleIntent = PendingIntent.getActivity(
            appContext, 0,
            intent,
            flagUpdateCurrent(true)
        )

        val person = Person.Builder()
            .setName(sender)
            .setIcon(icon)
            .setImportant(true)
            .build()

        val shortcut = ShortcutInfo.Builder(appContext, sender)
            .setShortLabel(sender)
            .setLongLived(true)
            .setIntent(shortcutIntent)
            .setIcon(icon)
            .setPerson(person)
            .build()

        val messagingStyle = Notification.MessagingStyle(person).addMessage(
            Notification.MessagingStyle.Message(message, System.currentTimeMillis(), person)
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            shortcutManager.pushDynamicShortcut(shortcut)

            val bubbleMetadata = Notification.BubbleMetadata.Builder(bubbleIntent, icon)
                .setDesiredHeightResId(R.dimen.bubble_height)
                .build()

            val notification = Notification.Builder(appContext, CHANNEL_BUBBLE_ID)
                .setBubbleMetadata(bubbleMetadata)
                .setShortcutId(sender)
                .addPerson(person)
                .setStyle(messagingStyle)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(bubbleIntent) // fallback click action
                .build()

            notificationManager.notify(1, notification)
        } else {
            // Fallback standard notification (no bubble)
            val notification = Notification.Builder(appContext, NOTIFICATION_ID)
                .setContentTitle(sender)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setStyle(messagingStyle)
                .setContentIntent(bubbleIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(1, notification)
        }
    }

}