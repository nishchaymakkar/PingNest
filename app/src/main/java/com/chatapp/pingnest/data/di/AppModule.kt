package com.chatapp.pingnest.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.chatapp.pingnest.data.NotificationHelper
import com.chatapp.pingnest.data.di.AppModule.provideApiService
import com.chatapp.pingnest.data.di.AppModule.provideHttpClient
import com.chatapp.pingnest.data.di.AppModule.provideMessagingClient
import com.chatapp.pingnest.data.di.AppModule.provideNotificationHelper
import com.chatapp.pingnest.data.di.AppModule.provideProtoDataStore
import com.chatapp.pingnest.data.local.AppSettingsSerializer
import com.chatapp.pingnest.data.models.AppSettings
import com.chatapp.pingnest.data.network.PingNestApiService
import com.chatapp.pingnest.data.network.PingNestApiServiceImpl
import com.chatapp.pingnest.data.network.RealtimeMessagingClient
import com.chatapp.pingnest.data.network.StompMessagingClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
val Context.protoDataStore: DataStore<AppSettings> by dataStore(
    fileName = "app-settings.json",
    serializer = AppSettingsSerializer
)
object AppModule {


    fun provideProtoDataStore(context: Context): DataStore<AppSettings> {
        return context.protoDataStore
    }



    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(HttpTimeout) {
                connectTimeoutMillis = 10_000
                socketTimeoutMillis = 15_000
                requestTimeoutMillis = 15_000
            }
            install(Logging)
            install(WebSockets)
            install(ContentNegotiation) {
                json()
            }
        }
    }

    fun provideApiService(client: HttpClient): PingNestApiService {
        return PingNestApiServiceImpl(client)
    }
    fun provideNotificationHelper(context: Context): NotificationHelper {
        return NotificationHelper(context)
    }

    fun provideMessagingClient(notificationHelper: NotificationHelper): RealtimeMessagingClient {
        return StompMessagingClient(notificationHelper)
    }
}


val appModule = module {
    single { provideHttpClient() }
    single { provideMessagingClient(get()) }
    single { provideApiService(get()) }
    single { provideProtoDataStore(androidContext()) }
    single { provideNotificationHelper(androidContext()) }
}
