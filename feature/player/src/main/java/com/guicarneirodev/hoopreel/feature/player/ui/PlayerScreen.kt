package com.guicarneirodev.hoopreel.feature.player.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guicarneirodev.hoopreel.feature.player.presentation.PlayerUiState
import com.guicarneirodev.hoopreel.feature.player.presentation.PlayerViewModel
import com.guicarneirodev.hoopreel.feature.player.ui.player.BasketballOrange
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = koinViewModel(),
    videoId: String,
    onBackPressed: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showControls by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(true) }
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val youTubePlayerState = remember { mutableStateOf<YouTubePlayer?>(null) }
    var currentTime by remember { mutableFloatStateOf(0f) }
    var totalDuration by remember { mutableFloatStateOf(0f) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var orientation by remember { mutableIntStateOf(configuration.orientation) }

    LaunchedEffect(showControls) {
        if (showControls && !isLandscape) {
            delay(4000)
            showControls = false
        }
    }

    LaunchedEffect(configuration.orientation) {
        if (orientation != configuration.orientation) {
            orientation = configuration.orientation
            youTubePlayerState.value?.pause()
        }
    }

    LaunchedEffect(Unit) {
        showControls = true
    }

    LaunchedEffect(videoId) {
        viewModel.loadVideo(videoId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .then(
                if (!isLandscape) {
                    Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                showControls = !showControls
                                Log.d("PlayerScreen", "Tap detectado, showControls=$showControls")
                            },
                            onDoubleTap = { position ->
                                val width = size.width
                                when {
                                    position.x < width * 0.3f -> {
                                        youTubePlayerState.value?.seekTo(maxOf(currentTime - 10, 0f))
                                    }
                                    position.x > width * 0.7f -> {
                                        youTubePlayerState.value?.seekTo(currentTime + 10)
                                    }
                                    else -> {
                                        youTubePlayerState.value?.let { player ->
                                            if (isPlaying) {
                                                player.pause()
                                            } else {
                                                player.play()
                                            }
                                            isPlaying = !isPlaying
                                        }
                                    }
                                }
                            }
                        )
                    }
                } else {
                    Modifier
                }
            )
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

                            val iFramePlayerOptions = IFramePlayerOptions.Builder()
                                .controls(if (isLandscape) 1 else 0)
                                .build()

                            initialize(object : AbstractYouTubePlayerListener() {
                                override fun onReady(youTubePlayer: YouTubePlayer) {
                                    youTubePlayer.loadVideo(videoId, 0f)
                                    youTubePlayerState.value = youTubePlayer
                                }

                                override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                                    isPlaying = state == PlayerConstants.PlayerState.PLAYING
                                }

                                override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                                    currentTime = second
                                }

                                override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
                                    totalDuration = duration
                                }
                            }, iFramePlayerOptions)

                            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                                override fun onDestroy(owner: LifecycleOwner) {
                                    release()
                                }
                            })
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                AnimatedVisibility(
                    visible = showControls && !isLandscape,
                    enter = fadeIn(initialAlpha = 0f),
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = onBackPressed
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Voltar",
                                    tint = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
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

                            IconButton(onClick = {
                                val activity = context as? Activity
                                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Fullscreen,
                                    contentDescription = "Tela cheia",
                                    tint = Color.White
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            FloatingActionButton(
                                onClick = {
                                    youTubePlayerState.value?.seekTo(maxOf(currentTime - 10, 0f))
                                },
                                containerColor = Color.Black.copy(alpha = 0.6f),
                                contentColor = Color.White
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Replay10,
                                    contentDescription = "Voltar 10 segundos",
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            FloatingActionButton(
                                onClick = {
                                    youTubePlayerState.value?.let { player ->
                                        if (isPlaying) {
                                            player.pause()
                                        } else {
                                            player.play()
                                        }
                                        isPlaying = !isPlaying
                                    }
                                },
                                containerColor = BasketballOrange,
                                contentColor = Color.White,
                                modifier = Modifier.size(64.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                    contentDescription = if (isPlaying) "Pausar" else "Reproduzir",
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            FloatingActionButton(
                                onClick = {
                                    youTubePlayerState.value?.seekTo(currentTime + 10)
                                },
                                containerColor = Color.Black.copy(alpha = 0.6f),
                                contentColor = Color.White
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Forward10,
                                    contentDescription = "Avançar 10 segundos",
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(16.dp)
                        ) {
                            Slider(
                                value = if (totalDuration > 0) currentTime / totalDuration else 0f,
                                onValueChange = { newValue ->
                                    val newTimeInSeconds = newValue * totalDuration
                                    youTubePlayerState.value?.seekTo(newTimeInSeconds)
                                },
                                colors = SliderDefaults.colors(
                                    thumbColor = BasketballOrange,
                                    activeTrackColor = BasketballOrange,
                                    inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${formatTime(currentTime.toLong())} / ${formatTime(totalDuration.toLong())}",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )

                                    Text(
                                        text = formatViewCount(video.statistics?.viewCount),
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = isLandscape,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        IconButton(
                            onClick = {
                                val activity = context as? Activity
                                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.FullscreenExit,
                                contentDescription = "Sair da tela cheia",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            is PlayerUiState.Error -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = state.message,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.loadVideo(videoId) },
                        colors = ButtonDefaults.buttonColors(containerColor = BasketballOrange)
                    ) {
                        Text("Tentar novamente")
                    }
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

@SuppressLint("DefaultLocale")
fun formatViewCount(viewCount: String?): String {
    if (viewCount == null) return "N/A"

    return try {
        val count = viewCount.toLong()
        when {
            count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
            count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
            else -> count.toString()
        }
    } catch (e: NumberFormatException) {
        viewCount
    }
}