package com.chatapp.pingnest.data.local

import androidx.datastore.core.Serializer
import com.chatapp.pingnest.data.models.AppSettings
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object AppSettingsSerializer : Serializer<AppSettings> {
    override val defaultValue: AppSettings
        get() = AppSettings()

    override suspend fun readFrom(input: InputStream): AppSettings {
        return try {
            Json.decodeFromString(
                deserializer = AppSettings.serializer(),
                string = input.readBytes().decodeToString()
            )

        } catch (e: SerializationException) {
            e.printStackTrace()
            // If deserialization fails, return the default settings, which is correct
            defaultValue
        }
    }
    override suspend fun writeTo(
        t: AppSettings,
        output: OutputStream
    ) {
        output.write(
            Json.encodeToString(
                serializer = AppSettings.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}