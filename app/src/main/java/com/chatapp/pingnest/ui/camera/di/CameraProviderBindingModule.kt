package com.chatapp.pingnest.ui.camera.di

import com.chatapp.pingnest.ui.camera.CameraProviderManager
import com.chatapp.pingnest.ui.camera.CameraXProcessCameraProviderManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module



val cameraModule = module {
    single{ CameraXProcessCameraProviderManager(androidContext()) }
}
