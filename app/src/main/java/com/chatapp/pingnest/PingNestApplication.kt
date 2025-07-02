package com.chatapp.pingnest

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.chatapp.pingnest.data.di.appModule
import com.chatapp.pingnest.ui.camera.di.cameraModule
import com.chatapp.pingnest.ui.photopicker.di.photoPickerModule
import com.chatapp.pingnest.ui.screens.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

const val CHANNEL_BUBBLE_ID = "bubble_channel"
const val NOTIFICATION_ID = "notification_channel"
class PingNestApplication: Application() {


    override fun onCreate() {
        super.onCreate()

            createNotificationChannel(this)

        startKoin {
            androidContext(this@PingNestApplication)
            androidLogger()
            modules(
                listOf(appModule, cameraModule,viewModelModule, photoPickerModule)
            )
        }
    }
}



fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        return
    }

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val channel: NotificationChannel

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        channel = NotificationChannel(
            CHANNEL_BUBBLE_ID,
            "Conversations",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for conversation bubbles"
            setAllowBubbles(true)

        }
    } else {

        channel = NotificationChannel(
            NOTIFICATION_ID,
            "General Notifications",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for general app notifications"
        }
    }

    notificationManager.createNotificationChannel(channel)
}
