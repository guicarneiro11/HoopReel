package com.guicarneirodev.hoopreel.feature.player.ui

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Fullscreen
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guicarneirodev.hoopreel.feature.player.BasketballOrange
import com.guicarneirodev.hoopreel.feature.player.presentation.PlayerUiState
import com.guicarneirodev.hoopreel.feature.player.presentation.PlayerViewModel
import com.guicarneirodev.hoopreel.feature.player.ui.player.LandscapeControls
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
    val lifecycleOwner = LocalLifecycleOwner.current
    val youTubePlayerState = remember { mutableStateOf<YouTubePlayer?>(null) }
    var currentTime by remember { mutableStateOf(0f) }
    var totalDuration by remember { mutableStateOf(0f) }

    // Detectar orientação da tela
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Resetar timer de controles quando showControls mudar para true
    LaunchedEffect(showControls) {
        if (showControls) {
            delay(4000) // Esconder controles após 4 segundos de inatividade
            showControls = false
        }
    }

    // Garantir que controles apareçam inicialmente
    LaunchedEffect(Unit) {
        showControls = true
    }

    // Load video data
    LaunchedEffect(videoId) {
        viewModel.loadVideo(videoId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        // Um toque apenas mostra/esconde os controles
                        showControls = !showControls
                        Log.d("PlayerScreen", "Tap detectado, showControls=$showControls")
                    },
                    onDoubleTap = { position ->
                        // Duplo toque nas laterais avança/retrocede
                        // Duplo toque no centro aciona play/pause
                        val width = size.width
                        when {
                            position.x < width * 0.3f -> {
                                // Lado esquerdo - retroceder 10s
                                youTubePlayerState.value?.seekTo(maxOf(currentTime - 10, 0f))
                            }
                            position.x > width * 0.7f -> {
                                // Lado direito - avançar 10s
                                youTubePlayerState.value?.seekTo(currentTime + 10)
                            }
                            else -> {
                                // Centro - play/pause
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

                // YouTube Player
                AndroidView(
                    factory = { ctx ->
                        YouTubePlayerView(ctx).apply {
                            enableAutomaticInitialization = false

                            // Configurar e inicializar o player
                            val iFramePlayerOptions = IFramePlayerOptions.Builder()
                                .controls(0) // Esconder controles nativos
                                .build()

                            initialize(object : AbstractYouTubePlayerListener() {
                                override fun onReady(player: YouTubePlayer) {
                                    // Carregar o vídeo quando o player estiver pronto
                                    player.loadVideo(videoId, 0f)
                                    youTubePlayerState.value = player
                                }

                                override fun onStateChange(player: YouTubePlayer, state: PlayerConstants.PlayerState) {
                                    isPlaying = state == PlayerConstants.PlayerState.PLAYING
                                }

                                override fun onCurrentSecond(player: YouTubePlayer, second: Float) {
                                    currentTime = second
                                }

                                override fun onVideoDuration(player: YouTubePlayer, duration: Float) {
                                    totalDuration = duration
                                }
                            }, iFramePlayerOptions)

                            // Gerenciar o ciclo de vida
                            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                                override fun onDestroy(owner: LifecycleOwner) {
                                    release()
                                }
                            })
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Custom controls overlay - diferentes para portrait e landscape
                AnimatedVisibility(
                    visible = showControls,
                    enter = fadeIn(initialAlpha = 0f),
                    exit = fadeOut()
                ) {
                    if (isLandscape) {
                        // Usar controles simplificados para landscape
                        LandscapeControls(
                            isPlaying = isPlaying,
                            currentTime = currentTime,
                            totalDuration = totalDuration,
                            onSeekTo = { newPosition ->
                                youTubePlayerState.value?.seekTo(newPosition)
                            },
                            onPlayPause = {
                                youTubePlayerState.value?.let { player ->
                                    if (isPlaying) player.pause() else player.play()
                                    isPlaying = !isPlaying
                                }
                            },
                            onSeekBack = {
                                youTubePlayerState.value?.seekTo(maxOf(currentTime - 10, 0f))
                            },
                            onSeekForward = {
                                youTubePlayerState.value?.seekTo(currentTime + 10)
                            },
                            onExitFullscreen = {
                                val activity = context as? Activity
                                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            },
                            onBackPressed = onBackPressed
                        )
                    } else {
                        // Controles padrão para modo retrato (portrait)
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
                            // Top bar with player info and back button
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.TopCenter)
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Back button
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

                                // Video info
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

                                // Toggle fullscreen button
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

                            // Center controls
                            Row(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // Rewind 10s
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

                                // Play/Pause button (maior)
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

                                // Forward 10s
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

                            // Bottom controls with progress bar
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .padding(16.dp)
                            ) {
                                // Progress bar
                                Slider(
                                    value = if (totalDuration > 0) currentTime / totalDuration else 0f,
                                    onValueChange = { newValue ->
                                        // Converter valor relativo para segundos
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

                                // Time indicators and stats
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Current time / Total time
                                    Text(
                                        text = "${formatTime(currentTime.toLong())} / ${formatTime(totalDuration.toLong())}",
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )

                                    // View count
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

fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

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