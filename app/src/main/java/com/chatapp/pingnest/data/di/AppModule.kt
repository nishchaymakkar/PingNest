package com.chatapp.pingnest.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.chatapp.pingnest.data.di.AppModule.dataStore
import com.chatapp.pingnest.data.di.AppModule.provideApiService
import com.chatapp.pingnest.data.di.AppModule.provideHttpClient
import com.chatapp.pingnest.data.di.AppModule.provideMessagingClient
import com.chatapp.pingnest.data.local.DataStoreRepository
import com.chatapp.pingnest.data.network.KtorStompMessagingClient
import com.chatapp.pingnest.data.network.PingNestApiService
import com.chatapp.pingnest.data.network.RealtimeMessagingClient
import com.chatapp.pingnest.data.network.StompMessagingClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.logging.Logger
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object AppModule {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_prefs")
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO){
            install(HttpTimeout){
                connectTimeoutMillis = 10_000 // 10 seconds
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
        return KtorStompMessagingClient(client)
    }
    fun provideMessagingClient(): RealtimeMessagingClient {
        return StompMessagingClient()
    }
}

val appModule = module {
    single { provideHttpClient()  }
    single { provideMessagingClient() }
    single { provideApiService(get()) }
    single { androidContext().dataStore }
    single{ DataStoreRepository(get()) }
}