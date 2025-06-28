package com.chatapp.pingnest.ui.camera

import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.camera.viewfinder.core.ImplementationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun ViewFinder(
    cameraState: CameraState,
    surfaceRequest: SurfaceRequest?,
    onZoomChange: (Float) -> Unit
) {
    val transformableState = rememberTransformableState(
        onTransformation = { zoomChange, _, _ ->
            onZoomChange(zoomChange)
        }
    )
    Box(
        Modifier.background(Color.Black)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .transformable(state = transformableState)
        ){
            surfaceRequest?.let {
                CameraXViewfinder(
                    modifier = Modifier.fillMaxSize(),
                    implementationMode = ImplementationMode.EXTERNAL,
                    surfaceRequest = surfaceRequest,
                )
            }
        }
    }
}