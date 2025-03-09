package com.guicarneirodev.hoopreel.feature.player.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guicarneirodev.hoopreel.feature.player.BasketballOrange
import com.guicarneirodev.hoopreel.feature.player.presentation.PlayerUiState
import com.guicarneirodev.hoopreel.feature.player.presentation.PlayerViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = koinViewModel(),
    videoId: String
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showControls by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(true) }
    var lifecycle = LocalLifecycleOwner.current.lifecycle
    val youTubePlayer = remember { mutableStateOf<YouTubePlayer?>(null) }

    LaunchedEffect(showControls) {
        if (showControls) {
            delay(3000) // Hide controls after 3 seconds of inactivity
            showControls = false
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

                AndroidView(
                    factory = { ctx ->
                        YouTubePlayerView(ctx).apply {
                            enableAutomaticInitialization = false

                            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                                override fun onReady(youTubePlayer: YouTubePlayer) {
                                    youTubePlayer.loadVideo(videoId, 0f)
                                    youTubePlayer.value = youTubePlayer
                                }

                                override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                                    isPlaying = state == PlayerConstants.PlayerState.PLAYING
                                }
                            })

                            getPlayerUiController().apply {
                                showFullscreenButton(false)
                                showYouTubeButton(false)
                                showSeekBar(false)
                                showVideoTitle(false)
                                showCurrentTime(false)
                                showDuration(false)
                            }

                            addFullScreenListener { isFullScreen ->
                                val activity = context as? Activity
                                activity?.requestedOrientation = if (isFullScreen) {
                                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                                } else {
                                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                                }
                            }

                            (ctx as? LifecycleOwner)?.let {
                                lifecycle = it.lifecycle
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Custom controls overlay
                AnimatedVisibility(
                    visible = showControls,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.7f),
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    ) {
                        // Top bar with player info
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Back button
                            IconButton(
                                onClick = { /* Handle back navigation */ }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // Video info
                            Column {
                                Text(
                                    text = viewModel.getPlayerName(videoId),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )

                                Text(
                                    text = video.title,
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Center controls
                        Row(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            IconButton(onClick = {
                                youTubePlayer.value?.seekTo((youTubePlayer.value?.currentSecond ?: 0f) - 10)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Replay10,
                                    contentDescription = "Rewind 10 seconds",
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            IconButton(
                                onClick = {
                                    youTubePlayer.value?.let { player ->
                                        if (isPlaying) {
                                            player.pause()
                                        } else {
                                            player.play()
                                        }
                                    }
                                },
                                modifier = Modifier.size(56.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                    contentDescription = if (isPlaying) "Pause" else "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(48.dp)
                                )
                            }

                            IconButton(onClick = {
                                youTubePlayer.value?.seekTo((youTubePlayer.value?.currentSecond ?: 0f) + 10)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Forward10,
                                    contentDescription = "Forward 10 seconds",
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val viewCount = formatViewCount(video.statistics?.viewCount)
                            Text(
                                text = viewCount,
                                color = Color.White,
                                fontSize = 14.sp
                            )

                            IconButton(onClick = {
                                val activity = context as? Activity
                                activity?.requestedOrientation = if (activity?.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                                } else {
                                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Fullscreen,
                                    contentDescription = "Toggle fullscreen",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
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

private fun formatViewCount(viewCount: String?): String {
    if (viewCount == null) return "N/A views"

    return try {
        val count = viewCount.toLong()
        when {
            count >= 1_000_000 -> String.format("%.1fM views", count / 1_000_000.0)
            count >= 1_000 -> String.format("%.1fK views", count / 1_000.0)
            else -> "$count views"
        }
    } catch (e: NumberFormatException) {
        "$viewCount views"
    }
}