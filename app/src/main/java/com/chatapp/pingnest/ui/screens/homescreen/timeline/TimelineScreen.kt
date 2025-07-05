package com.chatapp.pingnest.ui.screens.homescreen.timeline

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chatapp.pingnest.R
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import kotlin.math.absoluteValue
import androidx.core.net.toUri

@OptIn(UnstableApi::class)
@Composable
fun TimelineScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<TimelineViewModel>()
    val media = viewModel.media
    val player = viewModel.player
    val videoRatio = viewModel.videoRatio
    Box(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ){
        if (media.isEmpty()){
            EmptyTimeLine()
        } else {
            TimelineVerticalPager(
                mediaItems = media,
                player = player,
                onInitializePlayer = viewModel::initializePlayer,
                onReleasePlayer = viewModel::releasePlayer,
                onChangePlayerItem = viewModel::changePlayerItem,
                videoRatio = videoRatio
            )
        }
    }
}

@Composable
fun TimelineVerticalPager(
    modifier: Modifier = Modifier,
    mediaItems: List<TimelineMediaItem>,
    player: Player?,
    onInitializePlayer:() -> Unit,
    onReleasePlayer: () -> Unit,
    onChangePlayerItem: (uri: Uri?, page: Int) -> Unit,
    videoRatio: Float?
) {
    val pagerState = rememberPagerState(pageCount = { mediaItems.count() })
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { page->
            if (mediaItems[page].type == TimelineMediaType.VIDEO){
                onChangePlayerItem(Uri.parse(mediaItems[page].uri), pagerState.currentPage)
            } else {
                onChangePlayerItem(null,pagerState.currentPage)
            }
        }
    }

    val currentOnInitializePlayer by rememberUpdatedState(onInitializePlayer)
    val currentOnReleasePlayer by rememberUpdatedState(onReleasePlayer)
    if (Build.VERSION.SDK_INT > 23){
        LifecycleStartEffect(true) {
            currentOnInitializePlayer()
            onStopOrDispose {
                currentOnReleasePlayer()
            }
        }
    } else {
        LifecycleResumeEffect(true) {
            currentOnInitializePlayer()
            onPauseOrDispose {
                currentOnReleasePlayer()
            }
        }
    }
    VerticalPager(
        state = pagerState,
        modifier.fillMaxSize()
    ) { page ->
        if (player != null){
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .graphicsLayer {
                    val pagerOffset = (
                            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction ).absoluteValue

                    alpha = lerp(
                        start = 0f,
                        stop= 1f,
                        fraction = 1f - pagerOffset.coerceIn(0f, 1f)
                    )
                }
            ){
                TimelinePage(
                    modifier = Modifier.align(Alignment.Center),
                    media = mediaItems[page],
                    player = player,
                    videoRatio = videoRatio,
                    state = pagerState,
                    page = page
                )

                MetadataOverlay(Modifier.padding(16.dp), mediaItem = mediaItems[page])
            }
        }
    }

}

@Composable
fun TimelinePage(
    media: TimelineMediaItem,
    player: Player,
    page: Int,
    state: PagerState,
    videoRatio: Float?,
    modifier: Modifier = Modifier
) {
    when (media.type) {
        TimelineMediaType.VIDEO -> {
            if(page == state.settledPage){
                if (LocalInspectionMode.current){
                    Box(modifier)
                    return
                }

                AndroidView(
                    factory = { PlayerView(it)},
                    update = {playerView ->
                        playerView.player = player
                    }, modifier = modifier.fillMaxSize()
                )
            }
        }
        TimelineMediaType.PHOTO ->  {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(media.uri)
                    .build(),
                contentDescription = null,
                modifier = modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun MetadataOverlay(modifier: Modifier = Modifier, mediaItem: TimelineMediaItem) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .zIndex(999f)
    ) {
        if (mediaItem.type == TimelineMediaType.VIDEO) {
            val context = LocalContext.current
            val duration: State<Long?> = produceState(null) {
                withContext(Dispatchers.IO) {
                    try {
                        val retriever = MediaMetadataRetriever()
                        val uri = mediaItem.uri.toUri()
                        val pfd = context.contentResolver.openFileDescriptor(uri, "r")
                        pfd?.use {
                            retriever.setDataSource(it.fileDescriptor)
                            value = retriever
                                .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                                ?.toLong()
                        }
                    } catch (e: Exception) {
                        Log.e("MetadataOverlay", "Failed to get duration: ${e.localizedMessage}")
                        value = null
                    }
                }
            }

            duration.value?.let {
                val seconds = it / 1000L
                val minutes = seconds / 60L
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "%d:%02d".format(minutes, seconds % 60),
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomStart)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            mediaItem.chatIconUri?.let {
                Image(
                    painter = rememberIconPainter(contentUri = it),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                )
            }
            Text(modifier = Modifier.padding(end = 16.dp), text = mediaItem.chatName)
        }
    }
}


@Composable
fun EmptyTimeLine(modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
            .padding(64.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(painter = painterResource(R.drawable.empty_timeline),
            contentDescription = null)
        Text(
            text = "No photos or video",
            modifier = Modifier.padding(top = 64.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Videos and photos form messages will display here. Add....",
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun rememberIconPainter(contentUri: Uri): Painter {
    val icon = IconCompat.createWithAdaptiveBitmapContentUri(contentUri)
    val context = LocalContext.current
    return rememberDrawablePainter(drawable = icon.loadDrawable(context))
}
