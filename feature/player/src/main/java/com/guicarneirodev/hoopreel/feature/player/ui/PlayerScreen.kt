package com.guicarneirodev.hoopreel.feature.player.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.guicarneirodev.hoopreel.feature.player.BasketballOrange
import com.guicarneirodev.hoopreel.feature.player.presentation.PlayerUiState
import com.guicarneirodev.hoopreel.feature.player.presentation.PlayerViewModel
import com.guicarneirodev.hoopreel.feature.player.ui.player.CustomVideoControls
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = koinViewModel(),
    videoId: String
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val player = remember { ExoPlayer.Builder(context).build() }
    var showControls by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(true) }

    // Hide controls after a delay
    LaunchedEffect(showControls) {
        if (showControls) {
            delay(3000) // Hide controls after 3 seconds of inactivity
            showControls = false
        }
    }

    DisposableEffect(player) {
        onDispose {
            player.release()
        }
    }

    LaunchedEffect(videoId) {
        viewModel.loadVideo(videoId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { showControls = !showControls }
    ) {
        when (val state = uiState) {
            is PlayerUiState.Initial -> {}

            is PlayerUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = BasketballOrange
                )
            }

            is PlayerUiState.Success -> {
                val video = state.video

                // Initialize player
                LaunchedEffect(video) {
                    player.setMediaItem(MediaItem.fromUri(video.videoUrl))
                    player.prepare()
                    player.play()
                    isPlaying = true
                }

                // YouTube player view
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            this.player = player
                            useController = false // Disable default controls
                            setShutterBackgroundColor(Color.Black.toArgb())
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Custom controls overlay
                CustomVideoControls(
                    player = player,
                    videoTitle = video.title,
                    playerName = viewModel.getPlayerName(videoId),
                    viewCount = "1.2M views", // You can fetch this from API
                    isPlaying = isPlaying,
                    onPlayPauseClick = {
                        if (isPlaying) {
                            player.pause()
                        } else {
                            player.play()
                        }
                        isPlaying = !isPlaying
                    },
                    onForwardClick = {
                        player.seekTo(player.currentPosition + 10000) // 10 seconds
                        showControls = true
                    },
                    onRewindClick = {
                        player.seekTo(player.currentPosition - 10000) // 10 seconds
                        showControls = true
                    },
                    onFullscreenClick = {
                        // Toggle fullscreen mode
                        val activity = context as? Activity
                        activity?.requestedOrientation = if (activity?.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        } else {
                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        }
                    },
                    showControls = showControls
                )
            }

            is PlayerUiState.Error -> {
                Text(
                    text = state.message,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
        }
    }
}