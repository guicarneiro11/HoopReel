package com.guicarneirodev.hoopreel.feature.player.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guicarneirodev.hoopreel.feature.player.domain.model.Video
import com.guicarneirodev.hoopreel.feature.player.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val repository: PlayerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<PlayerUiState>(PlayerUiState.Initial)
    val uiState = _uiState.asStateFlow()

    fun loadVideo(videoId: String) {
        viewModelScope.launch {
            _uiState.value = PlayerUiState.Loading
            try {
                val video = repository.getVideoDetails(videoId)
                _uiState.value = PlayerUiState.Success(video)
            } catch (e: Exception) {
                _uiState.value = PlayerUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class PlayerUiState {
    data object Initial : PlayerUiState()
    data object Loading : PlayerUiState()
    data class Success(val video: Video) : PlayerUiState()
    data class Error(val message: String) : PlayerUiState()
}