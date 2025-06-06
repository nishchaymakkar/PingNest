package com.chatapp.pingnest

import android.app.Application
import com.chatapp.pingnest.data.di.appModule
import com.chatapp.pingnest.ui.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PingNestApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PingNestApplication)
            androidLogger()
            modules(
                listOf(appModule, viewModelModule)
            )
        }
    }
}