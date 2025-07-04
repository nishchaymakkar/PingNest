package com.chatapp.pingnest.ui.player.preloadmanger

import android.content.Context
import android.net.Uri
import android.os.Looper
import androidx.annotation.MainThread
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRendererCapabilitiesList
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.preload.DefaultPreloadManager
import androidx.media3.exoplayer.source.preload.DefaultPreloadManager.Status.STAGE_LOADED_FOR_DURATION_MS
import androidx.media3.exoplayer.source.preload.TargetPreloadStatusControl
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import com.chatapp.pingnest.ui.screens.homescreen.timeline.TimelineMediaItem

@UnstableApi
class PreloadMangerWrapper private constructor(
    private val defaultPreloadManager: DefaultPreloadManager
) {

    private val preloadWindow: ArrayDeque<Pair<MediaItem, Int>> = ArrayDeque()
    private var preloadWindowMaxSize = 5
    private var currentPlayingIndex = C.INDEX_UNSET

    private var mediaItemsList = listOf<TimelineMediaItem>()

    private val itemsRemainingToStartNextPreloading = 2

    companion object {
        fun build(
            playbackLooper: Looper,
            loadControl: DefaultLoadControl,
            context: Context
        ): PreloadMangerWrapper {
            val trackSelector = DefaultTrackSelector(context)

            trackSelector.init({}, DefaultBandwidthMeter.getSingletonInstance(context))

            val renderersFactory = DefaultRenderersFactory(context)

            val preloadManager = DefaultPreloadManager(
                PreloadStatusControl(),
                DefaultMediaSourceFactory(context),
                trackSelector,
                DefaultBandwidthMeter.getSingletonInstance(context),
                DefaultRendererCapabilitiesList.Factory(renderersFactory),
                loadControl.allocator,
                playbackLooper,
            )

            return PreloadMangerWrapper(preloadManager)
        }
    }

    fun init(mediaList: List<TimelineMediaItem>){
        if (mediaList.isEmpty()){
            return
        }
        setCurrentPlayingIndex(0)
        setMediaList(mediaList)
        preloadNextItems()
    }

    fun setCurrentPlayingIndex(currentPlayingItemIndex: Int){
        currentPlayingIndex = currentPlayingItemIndex
        defaultPreloadManager.setCurrentPlayingIndex(currentPlayingIndex)
        preloadNextItems()
    }

    private fun setMediaList(mediaList: List<TimelineMediaItem>){
        mediaItemsList = mediaList
    }

    private fun preloadNextItems(){
        var lastPreloadIndex = 0
        if (!preloadWindow.isEmpty()){
            lastPreloadIndex = preloadWindow.last().second
        }

        if (lastPreloadIndex - currentPlayingIndex <= itemsRemainingToStartNextPreloading){
            for (i in 1 until (preloadWindowMaxSize - itemsRemainingToStartNextPreloading)){
                addMediaItem(index = lastPreloadIndex + i)
                removeMediaItem()
            }
        }
    }

    private fun removeMediaItem(){
        if (preloadWindow.size <= preloadWindowMaxSize){
            return
        }
        val itemAndIndex = preloadWindow.removeFirst()
        defaultPreloadManager.remove(itemAndIndex.first)
    }

    private fun addMediaItem(index: Int){
        if (index < 0 || index >= mediaItemsList.size){
            return
        }
        val mediaItem = (MediaItem.fromUri(mediaItemsList[index].uri))
        defaultPreloadManager.add(mediaItem,index)
        preloadWindow.addLast(Pair(mediaItem,index))
    }

    fun setPreloadWindowSize(size: Int){
        preloadWindowMaxSize = size
    }
    @MainThread
    fun release(){
        defaultPreloadManager.release()
        preloadWindow.clear()
        mediaItemsList.toMutableList().clear()
    }

    fun getMediaSource(mediaItem: MediaItem): MediaSource? {
        return defaultPreloadManager.getMediaSource(mediaItem)
    }
    @UnstableApi
    class PreloadStatusControl : TargetPreloadStatusControl<Int> {
        override fun getTargetPreloadStatus(rankingData: Int): DefaultPreloadManager.Status {
            // By default preload first 3 seconds of the video
            return DefaultPreloadManager.Status(STAGE_LOADED_FOR_DURATION_MS, 3000L)
        }
    }
}