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
import com.chatapp.pingnest.BubbleActivity
import com.chatapp.pingnest.PingNestActivity
import com.chatapp.pingnest.R
import com.chatapp.pingnest.ui.CHANNEL_BUBBLE_ID

class NotificationHelper(context: Context) {

    val appContext = context.applicationContext!!

    private fun flagUpdateCurrent(mutable: Boolean): Int {
        return if (mutable) {
            if (Build.VERSION.SDK_INT >= 31) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        }
    }
    @RequiresApi(Build.VERSION_CODES.N_MR1)
    val shortcutManager =
        context.getSystemService(ShortcutManager::class.java)!!

    val notificationManager = context.getSystemService(NotificationManager::class.java)!!



    val target = Intent(context.applicationContext, BubbleActivity::class.java)
    val bubbleIntent = PendingIntent.getActivity(context,0,target,flagUpdateCurrent(false))!!

    val icon = Icon.createWithResource(context,R.drawable.bubble_chat)
    @RequiresApi(Build.VERSION_CODES.R)
    val bubbleMetadata = Notification.BubbleMetadata.Builder(bubbleIntent,icon)
        .setDesiredHeightResId(R.dimen.bubble_height)
        .build()


    val shortcutIntent = Intent(context, PingNestActivity::class.java).apply {
        action = Intent.ACTION_VIEW
    }





    @RequiresApi(Build.VERSION_CODES.Q)
    val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Notification.Builder(context,CHANNEL_BUBBLE_ID)
            .setBubbleMetadata(bubbleMetadata)
    } else {
        Notification.Builder(context,CHANNEL_BUBBLE_ID)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun sendNotification(message: String,sender: String){

        val person = Person.Builder( )
            .setName(sender)
            .setIcon(icon)
            .setImportant(true)
            .build()


        val shortcut = ShortcutInfo.Builder(appContext, sender)
            .setLongLived(true)
            .setIntent(
                shortcutIntent
            )
            .setShortLabel(sender)
            .setIcon(icon)
            .setPerson(person)
            .build()


        shortcutManager.pushDynamicShortcut(
            shortcut

        )

        notificationManager.notify(1, builder
            .setShortcutId(shortcut.id).addPerson(person)
            .setStyle(
                Notification.MessagingStyle(person).addMessage(
                    Notification.MessagingStyle.Message(
                        message,
                        System.currentTimeMillis(),
                        person
                    )
                ))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build())

    }


}