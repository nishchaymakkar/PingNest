package com.chatapp.pingnest.ui.videoedit

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeMute
import androidx.compose.material.icons.automirrored.outlined.VolumeOff
import androidx.compose.material.icons.filled.Brightness1
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DonutLarge
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.outlined.Brightness1
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.DonutLarge
import androidx.compose.material.icons.outlined.FormatSize
import androidx.compose.material.icons.outlined.VolumeMute
import androidx.compose.material.icons.outlined.VolumeOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.chatapp.pingnest.R
import org.koin.compose.viewmodel.koinViewModel
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.transformer.Composition
import androidx.media3.transformer.CompositionPlayer
import com.chatapp.pingnest.ui.theme.PingNestTheme
import com.chatapp.pingnest.ui.videoedit.VideoEditFilterChip

@Immutable
private data class VideoPreviewConfig(
    val uri: String,
    val removeAudioEnabled: Boolean,
    val rgbAdjustmentEffectEnabled: Boolean,
    val periodicVignetteEffectEnabled: Boolean,
//    val styleTransferEffectEnabled: Boolean,
    val overlayText: String,
    val redOverlayTextEnabled: Boolean,
    val largeOverlayTextEnabled: Boolean,
)

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoEditScreen(
    uri: String,
    onCloseButtonClicked: () -> Unit,
    onSendButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val viewModel: VideoEditScreenViewModel = koinViewModel()
    val isProcessing = viewModel.isProcessing.collectAsState()
    val isFinishedEditing = viewModel.isFinishedEditing.collectAsStateWithLifecycle()

    var removeAudioEnabled by rememberSaveable { mutableStateOf(false) }
    var rgbAdjustmentEffectEnabled by rememberSaveable { mutableStateOf(false) }
    var periodicVignetteEffectEnabled by rememberSaveable { mutableStateOf(false) }
    var styleTransferEffectEnabled by rememberSaveable { mutableStateOf(false) }
    var overlayText by rememberSaveable { mutableStateOf("") }
    var redOverlayTextEnabled by rememberSaveable { mutableStateOf(false) }
    var largeOverlayTextEnabled by rememberSaveable { mutableStateOf(false) }

    val previewConfig = remember(
        removeAudioEnabled,
        rgbAdjustmentEffectEnabled,
        periodicVignetteEffectEnabled,
        styleTransferEffectEnabled,
        overlayText,
        redOverlayTextEnabled,
        largeOverlayTextEnabled,
    ) {
        VideoPreviewConfig(
            uri = uri,
            removeAudioEnabled = removeAudioEnabled,
            rgbAdjustmentEffectEnabled = rgbAdjustmentEffectEnabled,
            periodicVignetteEffectEnabled = periodicVignetteEffectEnabled,
          //  styleTransferEffectEnabled = styleTransferEffectEnabled,
            overlayText = overlayText,
            redOverlayTextEnabled = redOverlayTextEnabled,
            largeOverlayTextEnabled = largeOverlayTextEnabled,
        )
    }

    Scaffold (
        topBar = {
            VideoEditTopAppBar(
                onSendButtonClicked = {
                    viewModel.applyVideoTransformation(
                        context = context,
                        videoUri = uri,
                        removeAudio = removeAudioEnabled,
                        rgbAdjustmentEffectSelected = rgbAdjustmentEffectEnabled,
                        periodicVignetteEffectSelected = periodicVignetteEffectEnabled,
//                        styleTransferEffectSelected = styleTransferEffectEnabled,
                        textOverlayText = overlayText,
                        textOverlayRedSelected = redOverlayTextEnabled,
                        textOverlayLargeSelected = largeOverlayTextEnabled,

                        )
                },
                onCloseButtonClicked = onCloseButtonClicked,
                onMuteClicked = { removeAudioEnabled = !removeAudioEnabled},
                muteSelected = removeAudioEnabled,
                onColorClicked = { rgbAdjustmentEffectEnabled = !rgbAdjustmentEffectEnabled},
                colorSelected = rgbAdjustmentEffectEnabled,
                onRedTextClicked = { redOverlayTextEnabled = !redOverlayTextEnabled},
                redTextCheckedState = redOverlayTextEnabled,
                largeTextCheckedState = largeOverlayTextEnabled,
                largeTextCheckedStateChange = { largeOverlayTextEnabled = !largeOverlayTextEnabled},
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color.Black)
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center,
        ) {
           VideoMessagePreview(
                context,
                previewConfig,
            ) { context, previewConfig ->
                // Trigger composition preparation process in the viewModel
                viewModel.prepareComposition(
                    context = context,
                    videoUri = previewConfig.uri,
                    removeAudio = previewConfig.removeAudioEnabled,
                    rgbAdjustmentEffectSelected = previewConfig.rgbAdjustmentEffectEnabled,
                    periodicVignetteEffectSelected = previewConfig.periodicVignetteEffectEnabled,
                    //styleTransferEffectSelected = previewConfig.styleTransferEffectEnabled,
                    textOverlayText = previewConfig.overlayText,
                    textOverlayRedSelected = previewConfig.redOverlayTextEnabled,
                    textOverlayLargeSelected = previewConfig.largeOverlayTextEnabled,
                )
            }

            Column(
                modifier = Modifier
                    .background(
                        color = colorResource(R.color.black),
                        shape = RoundedCornerShape(size = 28.dp),
                    )
                    .padding(15.dp)
                    .align(Alignment.BottomCenter),
            ) {
                Spacer(modifier = Modifier.height(10.dp))
               TextOverlayOption(
                    inputtedText = overlayText,
                    inputtedTextChange = {
                        // Limit character count to 20
                        if (it.length <= 20) {
                            overlayText = it
                        }
                    },
                )
            }
        }
    }

    CenteredCircularProgressIndicator(isProcessing.value)

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideoEditTopAppBar(
    onSendButtonClicked: ()-> Unit,
    onCloseButtonClicked: ()-> Unit,
    onMuteClicked: ()-> Unit,
    muteSelected: Boolean,
    onColorClicked: ()-> Unit,
    colorSelected: Boolean,
    onRedTextClicked: ()-> Unit,
    redTextCheckedState: Boolean,
    largeTextCheckedState: Boolean,
    largeTextCheckedStateChange: ()-> Unit,
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = onCloseButtonClicked
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null
                )
            }
        },
        actions = {
            VideoEditIcon(
                icon = if (muteSelected) Icons.AutoMirrored.Outlined.VolumeOff else Icons.AutoMirrored.Outlined.VolumeMute,
                selected = muteSelected,
                onClick = onMuteClicked,
                label = "Mute"
            )
            VideoEditIcon(
                icon = if (colorSelected) Icons.Filled.ColorLens else Icons.Outlined.ColorLens,
                selected = colorSelected,
                onClick = onColorClicked,
                label = "Color"
            )
            VideoEditIcon(
                icon = if (redTextCheckedState) Icons.Filled.DonutLarge else Icons.Outlined.DonutLarge,
                selected = redTextCheckedState,
                onClick = onRedTextClicked,
                label = "Red"
            )
            VideoEditIcon(
                icon = if (largeTextCheckedState) Icons.Filled.FormatSize else Icons.Outlined.FormatSize,
                selected = largeTextCheckedState,
                onClick = largeTextCheckedStateChange,
                label = "Large"
            )
           
            Button(
                onClick = onSendButtonClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.teal_200),
                    contentColor = Color.Black
                )
            ) {
                Text(text = "Send", fontWeight = FontWeight.SemiBold)
            }

        }
    )
}


@SuppressLint("RestrictedApi")
@UnstableApi
@Composable
private fun VideoMessagePreview(
    context: Context,
    previewConfig: VideoPreviewConfig,
    prepareComposition: (Context, VideoPreviewConfig) -> Composition,
) {

    if (LocalInspectionMode.current) {
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(266.dp)
                .background(color = Color.Yellow),
        )
        return
    }

    val playerView = remember(context) { PlayerView(context) }
    var compositionPlayer by remember { mutableStateOf<CompositionPlayer?>(null) }

    AndroidView(
        factory = {
            playerView.apply {
                player = compositionPlayer
                controllerAutoShow = false
            }
        },
        modifier = Modifier.width(300.dp).height(500.dp),
    )

    LaunchedEffect(previewConfig) {
        compositionPlayer?.release()

        compositionPlayer = CompositionPlayer.Builder(context).build()
        playerView.player = compositionPlayer

        val composition: Composition = prepareComposition(context, previewConfig)
        compositionPlayer?.setComposition(composition)
        compositionPlayer?.prepare()
        compositionPlayer?.play()
    }

}

@Composable
fun TextOverlayOption(
    inputtedText: String,
    inputtedTextChange: (String) -> Unit,
) {
    val localFocusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = inputtedText,
            onValueChange = inputtedTextChange,
            trailingIcon = {
                IconButton(
                    onClick = { localFocusManager.clearFocus()}
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Done,
                        contentDescription = null
                    )
                }
            },
            placeholder = { Text("Add text overlay") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.DarkGray,
                unfocusedPlaceholderColor = Color.LightGray,
            ),
        )
        Spacer(modifier = Modifier.padding(5.dp))
      /*  Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            VideoEditFilterChip(
                icon = Icons.Filled.DonutLarge,
                selected = redTextCheckedState,
                onClick = redTextCheckedStateChange,
                label = "Red",
                iconColor = Color.Red,
            )
            Spacer(modifier = Modifier.padding(10.dp))

            VideoEditFilterChip(
                icon = Icons.Filled.FormatSize,
                selected = largeTextCheckedState,
                onClick = largeTextCheckedStateChange,
                label = "Large",
            )
        }*/
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideoEditFilterChip(
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    iconColor: Color = Color.White,
    selectedIconColor: Color = Color.Black,
) {
    FilterChip(
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(FilterChipDefaults.IconSize),
            )
        },
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        colors = FilterChipDefaults.filterChipColors(
            labelColor = Color.White,
            selectedContainerColor = colorResource(id = R.color.purple_500),
            selectedLabelColor = Color.Black,
            iconColor = iconColor,
            selectedLeadingIconColor = selectedIconColor,
        ),
    )
}

@Composable
fun VideoEditIcon(
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    iconColor: Color = Color.White,
    selectedIconColor: Color = Color.White,
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(FilterChipDefaults.IconSize),
            tint = if (selected) selectedIconColor else iconColor
        )
    }
}
@Composable
private fun CenteredCircularProgressIndicator(isProcessing: Boolean) {
    if (isProcessing) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                    .padding(8.dp),
            )
        }
    }
}

@Preview
@Composable
private fun VideoEditScreenPreview() {
    PingNestTheme {
        VideoEditScreen(
            uri = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            onCloseButtonClicked = {},
            onSendButtonClicked = {},
        )
    }

}