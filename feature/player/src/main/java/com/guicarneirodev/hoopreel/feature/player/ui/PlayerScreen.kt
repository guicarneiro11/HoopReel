package com.guicarneirodev.hoopreel.feature.player.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guicarneirodev.hoopreel.feature.player.presentation.PlayerUiState
import com.guicarneirodev.hoopreel.feature.player.presentation.PlayerViewModel
import com.guicarneirodev.hoopreel.feature.player.ui.player.VideoPlayerComponent
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = koinViewModel(),
    videoId: String
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadVideo(videoId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is PlayerUiState.Initial -> Unit
            is PlayerUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is PlayerUiState.Success -> {
                VideoPlayerComponent(
                    video = state.video,
                    modifier = Modifier.fillMaxSize()
                )
            }
            is PlayerUiState.Error -> {
                Text(
                    text = state.message,
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}