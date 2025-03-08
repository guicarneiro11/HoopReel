package com.guicarneirodev.hoopreel.feature.player.ui.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.exoplayer.ExoPlayer

@Composable
fun CustomVideoControls(
    player: ExoPlayer,
    modifier: Modifier = Modifier,
    videoTitle: String,
    playerName: String,
    viewCount: String,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    onForwardClick: () -> Unit,
    onRewindClick: () -> Unit,
    onFullscreenClick: () -> Unit,
    showControls: Boolean
) {
    AnimatedVisibility(
        visible = showControls,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
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
            TopBar(
                playerName = playerName,
                videoTitle = videoTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            )

            // Center controls
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = onRewindClick) {
                    Icon(
                        imageVector = Icons.Filled.Replay10,
                        contentDescription = "Rewind 10 seconds",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(
                    onClick = onPlayPauseClick,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                IconButton(onClick = onForwardClick) {
                    Icon(
                        imageVector = Icons.Filled.Forward10,
                        contentDescription = "Forward 10 seconds",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
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
                // Video progress bar
                VideoProgressBar(
                    player = player,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom row with time and fullscreen
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${formatDuration(player.currentPosition)} / ${formatDuration(player.duration)}",
                        color = Color.White,
                        fontSize = 14.sp
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = viewCount,
                            color = Color.White,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        IconButton(onClick = onFullscreenClick) {
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
    }
}
