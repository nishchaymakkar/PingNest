package com.chatapp.pingnest.ui.screens.homescreen.timeline

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.HandlerThread
import android.os.Process
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import com.chatapp.pingnest.ui.player.preloadmanger.PreloadMangerWrapper
import kotlinx.coroutines.launch
fun getSampleVideoUris(context: Context, limit: Int = 2): List<Uri> {
    val videoUris = mutableListOf<Uri>()
    val projection = arrayOf(MediaStore.Video.Media._ID)
    val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

    val cursor = context.contentResolver.query(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        sortOrder
    )

    cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        while (it.moveToNext() && videoUris.size < limit) {
            val id = it.getLong(idColumn)
            val contentUri = ContentUris.withAppendedId(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                id
            )
            videoUris.add(contentUri)
        }
    }

    return videoUris
}



@UnstableApi
class TimelineViewModel (
    private val application: Context
): ViewModel() {
    var media by mutableStateOf<List<TimelineMediaItem>>(emptyList())

    var player by mutableStateOf<ExoPlayer?>(null)

    var videoRatio by mutableStateOf<Float?>(null)

    private val enablePreloadManager: Boolean = true
    private lateinit var preloadManager: PreloadMangerWrapper

    private val playerThread: HandlerThread =
        HandlerThread("playback-thread", Process.THREAD_PRIORITY_AUDIO)

    var playbackStartTimeMs = C.TIME_UNSET

    private val videoSizeListener = object : Player.Listener {
        override fun onVideoSizeChanged(videoSize: VideoSize) {
            videoRatio = if (videoSize.height > 0 && videoSize.width >0){
                videoSize.height.toFloat() / videoSize.width.toFloat()
            } else {
                null
            }
            super.onVideoSizeChanged(videoSize)
        }
    }
    val videoUris = getSampleVideoUris(application)

    val sampleTimelineData = listOf(
        TimelineMediaItem(
            uri = videoUris.getOrNull(0)?.toString() ?: "",
            type = TimelineMediaType.VIDEO,
            timeStamp = System.currentTimeMillis(),
            chatName = "Sample Media 1",
            chatIconUri = null
        ),
        TimelineMediaItem(
        uri = videoUris.getOrNull(1)?.toString() ?: "",
        type = TimelineMediaType.VIDEO,
        timeStamp = System.currentTimeMillis(),
        chatName = "Sample Media 2",
        chatIconUri = null
        ),
        TimelineMediaItem(
            uri = videoUris.getOrNull(2)?.toString() ?: "",
            type = TimelineMediaType.VIDEO,
            timeStamp = System.currentTimeMillis(),
            chatName = "Sample Media 3",
            chatIconUri = null
        ),

    )

    private val firstFrameListener = object : Player.Listener {
        override fun onRenderedFirstFrame() {
            val timeToFirstFrameMs = System.currentTimeMillis() - playbackStartTimeMs
            Log.d("Preload Manager", "\t\t Time to first Frame = $timeToFirstFrameMs")
            super.onRenderedFirstFrame()
        }
    }
    init {
        viewModelScope.launch {
            val newList = mutableListOf<TimelineMediaItem>()

            newList += sampleTimelineData
            newList.sortByDescending { it.timeStamp }
            media = newList
        }
    }
    fun initializePlayer(){
        if (player != null) return

        val loadControl = DefaultLoadControl.Builder().setBufferDurationsMs(5_000,20_000,5_00,
            DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS)
            .setPrioritizeTimeOverSizeThresholds(true).build()

        playerThread.start()

        val newPlayer = ExoPlayer
            .Builder(application.applicationContext)
            .setLoadControl(loadControl)
            .setPlaybackLooper(playerThread.looper)
            .build()
            .also {
                it.repeatMode = ExoPlayer.REPEAT_MODE_ONE
                it.playWhenReady = true
                it.addListener(videoSizeListener)
                it.addListener(firstFrameListener)
            }

        videoRatio = null
        player = newPlayer

        if (enablePreloadManager){
            initPreloadManager(loadControl,playerThread)
        }
    }
    private fun initPreloadManager(
        loadControl: DefaultLoadControl,
        preloadAndPlaybackThread: HandlerThread
    ){
        preloadManager = PreloadMangerWrapper.build(
            preloadAndPlaybackThread.looper,
            loadControl,
            application.applicationContext
        )

        preloadManager.setPreloadWindowSize(5)

        if (media.isNotEmpty()){
            preloadManager.init(media)
        }
    }

    fun releasePlayer(){
        if (enablePreloadManager){
            preloadManager.release()
        }

        player?.apply {
            removeListener(videoSizeListener)
            removeListener(firstFrameListener)
            release()
        }

        playerThread.quit()
        videoRatio = null
        player = null
    }

    fun changePlayerItem(uri: Uri?, currentPlayingIndex: Int){
        if (player == null) return

        player?.apply {
            stop()
            videoRatio = null
            if (uri != null){
                val mediaItem = MediaItem.fromUri(uri)

                if (enablePreloadManager){
                    val mediaSource = preloadManager.getMediaSource(mediaItem)

                    if (mediaSource == null){
                        setMediaItem(mediaItem)
                    } else {
                        setMediaSource(mediaSource)
                    }
                    preloadManager.setCurrentPlayingIndex(currentPlayingIndex)
                }else {
                    setMediaItem(mediaItem)
                }
                playbackStartTimeMs = System.currentTimeMillis()

                prepare()
            } else {
                clearMediaItems()
            }
        }
    }
}