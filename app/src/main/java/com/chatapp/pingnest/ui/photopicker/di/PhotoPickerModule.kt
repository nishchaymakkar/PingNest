package com.chatapp.pingnest.ui.photopicker.di

import android.content.ContentResolver
import android.content.Context
import com.chatapp.pingnest.ui.photopicker.di.PhotoPickerModule.provideContentResolver
import org.koin.dsl.module

object PhotoPickerModule {

    fun provideContentResolver(context: Context): ContentResolver =
        context.contentResolver
}

val photoPickerModule = module {
    single{ provideContentResolver(get())}
}