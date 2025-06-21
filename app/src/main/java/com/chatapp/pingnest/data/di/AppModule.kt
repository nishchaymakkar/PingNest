package com.chatapp.pingnest.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.chatapp.pingnest.data.di.AppModule.provideApiService
import com.chatapp.pingnest.data.di.AppModule.provideDataPrefs
import com.chatapp.pingnest.data.di.AppModule.provideDataStore
import com.chatapp.pingnest.data.di.AppModule.provideHttpClient
import com.chatapp.pingnest.data.di.AppModule.provideMessagingClient
import com.chatapp.pingnest.data.fake.FakeMessagingClient
import com.chatapp.pingnest.data.fake.FakePingNestApiService
import com.chatapp.pingnest.data.local.UserPreferencesRepository
import com.chatapp.pingnest.data.local.UserData
import com.chatapp.pingnest.data.network.PingNestApiServiceImpl
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
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object AppModule {
    fun provideDataStore(context: Context): DataStore<Preferences>{
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { context.preferencesDataStoreFile("user_data") }
        )
    }
    fun provideDataPrefs(dataStore: DataStore<Preferences>): UserData {
        return UserPreferencesRepository(dataStore)
    }
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
        return FakePingNestApiService()
    }
    fun provideMessagingClient(): RealtimeMessagingClient {
        return FakeMessagingClient()
    }
}

val appModule = module {
    single { provideHttpClient()  }
    single { provideMessagingClient() }
    single { provideApiService(get()) }
    single { provideDataStore(androidContext())  }
    single { provideDataPrefs(get()) }
    single{ UserPreferencesRepository(get()) }
}