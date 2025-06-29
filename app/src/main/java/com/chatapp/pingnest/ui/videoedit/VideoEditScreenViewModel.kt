package com.chatapp.pingnest.ui.videoedit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.media3.common.Effect
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.effect.ByteBufferGlEffect
import androidx.media3.effect.GlEffect
import androidx.media3.effect.OverlayEffect
import androidx.media3.effect.RgbAdjustment
import androidx.media3.effect.TextOverlay
import androidx.media3.effect.TextureOverlay
import androidx.media3.transformer.Composition
import androidx.media3.transformer.Composition.HDR_MODE_TONE_MAP_HDR_TO_SDR_USING_OPEN_GL
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.EditedMediaItemSequence
import androidx.media3.transformer.Effects
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.Transformer
import com.chatapp.pingnest.ui.camera.CameraViewModel
import com.google.common.collect.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

private const val TAG = "VideoEditViewModel"
@UnstableApi
class VideoEditScreenViewModel(
    private val application: Context
): ViewModel() {

    private val _isFinishedEditing = MutableStateFlow(false)
    val isFinishedEditing: StateFlow<Boolean> = _isFinishedEditing

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing
    private var transformedVideoFilePath = ""
    private val transformerListener: Transformer.Listener =
        object : Transformer.Listener {
            override fun onCompleted(
                composition: Composition,
                exportResult: ExportResult
            ) {
                Toast.makeText(application, "Edited video saved", Toast.LENGTH_LONG).show()

//                sendVideo()

                _isFinishedEditing.value = true
                _isProcessing.value = false
            }

            override fun onError(
                composition: Composition,
                exportResult: ExportResult,
                exportException: ExportException
            ) {
                exportException.printStackTrace()
                Toast.makeText(application, "Error applying edits on video", Toast.LENGTH_LONG)
                    .show()
                _isProcessing.value = false
            }
        }
    @OptIn(UnstableApi::class)
    fun applyVideoTransformation(
        context: Context,
        videoUri: String,
        removeAudio: Boolean,
        rgbAdjustmentEffectSelected: Boolean,
        periodicVignetteEffectSelected: Boolean,
//        styleTransferEffectSelected: Boolean,
        textOverlayText: String,
        textOverlayRedSelected: Boolean,
        textOverlayLargeSelected: Boolean,
    ) {
        val transformer = Transformer.Builder(context)
            .setVideoMimeType(MimeTypes.VIDEO_H264)
            .addListener(transformerListener)
            .build()

        val composition = prepareComposition(
            context = context,
            videoUri = videoUri,
            removeAudio = removeAudio,
            rgbAdjustmentEffectSelected = rgbAdjustmentEffectSelected,
            periodicVignetteEffectSelected = periodicVignetteEffectSelected,
//            styleTransferEffectSelected = styleTransferEffectSelected,
            textOverlayText = textOverlayText,
            textOverlayRedSelected = textOverlayRedSelected,
            textOverlayLargeSelected = textOverlayLargeSelected,
        )

        val editedVideoFileName = "PingNest-edited-recording-" +
                SimpleDateFormat(CameraViewModel.FILENAME_FORMAT, Locale.US)
                    .format(System.currentTimeMillis()) + ".mp4"

        transformedVideoFilePath = createNewVideoFilePath(context, editedVideoFileName)

        // TODO: Investigate using MediaStoreOutputOptions instead of external cache file for saving
        //  edited video https://github.com/androidx/media/issues/504
        transformer.start(composition, transformedVideoFilePath)
        _isProcessing.value = true
    }

    @OptIn(UnstableApi::class)
    fun prepareComposition(
        context: Context,
        videoUri: String,
        removeAudio: Boolean,
        rgbAdjustmentEffectSelected: Boolean,
        periodicVignetteEffectSelected: Boolean,
        //styleTransferEffectSelected: Boolean,
        textOverlayText: String,
        textOverlayRedSelected: Boolean,
        textOverlayLargeSelected: Boolean,
    ): Composition {
        val mediaItem = MediaItem.fromUri(videoUri)
        // Try to retrieve the video duration
        val retriever = MediaMetadataRetriever()
        val durationUs = try {
            retriever.setDataSource(context, videoUri.toUri())
            val durationStr =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            durationStr?.toLongOrNull()?.times(1000) ?: 0L
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving duration of video")
            0L
        } finally {
            retriever.release()
        }
        // Build the list of video effects to apply
        val videoEffects =
            buildVideoEffectsList(
                context = context,
                rgbAdjustmentEffectSelected = rgbAdjustmentEffectSelected,
                periodicVignetteEffectSelected = periodicVignetteEffectSelected,
//                styleTransferEffectSelected = styleTransferEffectSelected,
                textOverlayText = textOverlayText,
                textOverlayRedSelected = textOverlayRedSelected,
                textOverlayLargeSelected = textOverlayLargeSelected,
            )

        val editedMediaItem =
            EditedMediaItem.Builder(mediaItem)
                .setRemoveAudio(removeAudio).setDurationUs(durationUs).build()
        val videoImageSequence = EditedMediaItemSequence(editedMediaItem)

        val compositionBuilder = Composition.Builder(videoImageSequence)
        // Tone-map to SDR if style transfer is selected since it can only be applied for SDR videos
//        if (styleTransferEffectSelected) {
//            compositionBuilder.setHdrMode(HDR_MODE_TONE_MAP_HDR_TO_SDR_USING_OPEN_GL)
//        }
        compositionBuilder.setEffects(Effects(listOf(), videoEffects))
        return compositionBuilder.build()
    }


    @OptIn(UnstableApi::class)
    private fun buildVideoEffectsList(
        context: Context,
        rgbAdjustmentEffectSelected: Boolean,
        periodicVignetteEffectSelected: Boolean,
//        styleTransferEffectSelected: Boolean,
        textOverlayText: String,
        textOverlayRedSelected: Boolean,
        textOverlayLargeSelected: Boolean,
    ): List<Effect> {
        val videoEffects: MutableList<GlEffect> = mutableListOf()
        val overlaysBuilder = ImmutableList.Builder<TextureOverlay>()

        // Add text overlay effect if text is provided
        if (textOverlayText.isNotEmpty()) {
            val spannableStringBuilder = SpannableStringBuilder(textOverlayText)

            val spanStart = 0
            val spanEnd = textOverlayText.length

            val redTextSpan = ForegroundColorSpan(Color.RED)
            val doubleTextSpan = RelativeSizeSpan(2f)

            if (textOverlayRedSelected) {
                spannableStringBuilder.setSpan(
                    redTextSpan,
                    spanStart,
                    spanEnd,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE,
                )
            }
            if (textOverlayLargeSelected) {
                spannableStringBuilder.setSpan(
                    doubleTextSpan,
                    spanStart,
                    spanEnd,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE,
                )
            }

            val textOverlay = TextOverlay.createStaticTextOverlay(
                SpannableString.valueOf(spannableStringBuilder),
            )

            overlaysBuilder.add(textOverlay)
        }
        videoEffects.add(OverlayEffect(overlaysBuilder.build()))

        // Add RGB adjustment effect if selected
        if (rgbAdjustmentEffectSelected) {
            // Create an RgbAdjustment effect to modify the color balance
            videoEffects.add(
                RgbAdjustment.Builder()
                    .setRedScale(1.5f)
                    .setGreenScale(1.2f)
                    .setBlueScale(0.8f).build(),
            )
        }
        // Add periodic vignette effect if selected
        if (periodicVignetteEffectSelected) {
            // Create a GlEffect that applies a vignette effect that changes over time
            // The shader program for this effect is defined in PeriodicVignetteShaderProgram
            videoEffects.add(
                GlEffect { context: Context?, useHdr: Boolean ->
                    PeriodicVignetteShaderProgram(
                        context = context,
                        useHdr = useHdr,
                        centerX = 0.5f,
                        centerY = 0.5f,
                        minInnerRadius = 0f,
                        maxInnerRadius = 0.7f,
                        outerRadius = 0.7f,
                    )
                },
            )
        }
        // Add style transfer effect if selected
//        if (styleTransferEffectSelected) {
            // Apply a style transfer effect using a TensorFlow Lite model
//            videoEffects.add(ByteBufferGlEffect<Bitmap>(StyleTransferEffect(context, "style.jpg")))
//        }

        return videoEffects
    }

    private fun createNewVideoFilePath(context: Context, fileName: String): String {
        val externalCacheFile = createExternalCacheFile(context, fileName)
        return externalCacheFile.absolutePath
    }

    @Throws(IOException::class)
    private fun createExternalCacheFile(context: Context, fileName: String): File {
        val file = File(context.externalCacheDir, fileName)
        check(!(file.exists() && !file.delete())) {
            "Could not delete the previous transformer output file"
        }
        check(file.createNewFile()) { "Could not create the transformer output file" }
        return file
    }

}