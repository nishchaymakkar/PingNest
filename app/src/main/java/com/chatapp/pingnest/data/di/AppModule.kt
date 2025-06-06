package com.chatapp.pingnest.data.di

import com.chatapp.pingnest.data.di.AppModule.provideHttpClient
import com.chatapp.pingnest.data.di.AppModule.provideMessagingClient
import com.chatapp.pingnest.data.network.KtorStompMessagingClient
import com.chatapp.pingnest.data.network.RealtimeMessagingClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import org.koin.dsl.module

object AppModule {
    fun provideHttpClient(): HttpClient {
        return HttpClient{
            install(WebSockets)
            install(ContentNegotiation) {
                json()
            }
        }
    }
    fun provideMessagingClient(client: HttpClient): RealtimeMessagingClient {
        return KtorStompMessagingClient(client)
    }
}

val appModule = module {
    single { provideHttpClient()  }
    single { provideMessagingClient(get()) }
}