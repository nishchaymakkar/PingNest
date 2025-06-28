package com.chatapp.pingnest.ui.camera

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance

interface CameraProviderManager {
    suspend fun getCameraProvider(): ProcessCameraProvider
}

class CameraXProcessCameraProviderManager(
    appContext: Context,
) : CameraProviderManager{
    val context = appContext
    override suspend fun getCameraProvider()= ProcessCameraProvider.awaitInstance(context)
}