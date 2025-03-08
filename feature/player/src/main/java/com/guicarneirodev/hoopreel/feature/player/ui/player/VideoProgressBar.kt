package com.guicarneirodev.hoopreel.feature.player.ui.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.guicarneirodev.hoopreel.feature.player.BasketballOrange
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun VideoProgressBar(
    player: ExoPlayer,
    modifier: Modifier = Modifier
) {
    val progress = remember { mutableStateOf(0f) }
    val bufferedProgress = remember { mutableStateOf(0f) }

    LaunchedEffect(player) {
        while (isActive) {
            if (player.duration > 0) {
                progress.value = player.currentPosition.toFloat() / player.duration.toFloat()
                bufferedProgress.value = player.bufferedPosition.toFloat() / player.duration.toFloat()
            }
            delay(500) // Update every 500ms
        }
    }

    Box(modifier = modifier.height(20.dp)) {
        // Buffered progress background
        Slider(
            value = bufferedProgress.value,
            onValueChange = {},
            enabled = false,
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(
                disabledThumbColor = Color.Transparent,
                disabledActiveTrackColor = Color.Gray.copy(alpha = 0.5f),
                disabledInactiveTrackColor = Color.Gray.copy(alpha = 0.3f)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Actual progress
        Slider(
            value = progress.value,
            onValueChange = { newValue ->
                progress.value = newValue
                player.seekTo((newValue * player.duration).toLong())
            },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(
                thumbColor = BasketballOrange,
                activeTrackColor = BasketballOrange,
                inactiveTrackColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Utility function to format duration
fun formatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}