package com.guicarneirodev.hoopreel.feature.player.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guicarneirodev.hoopreel.feature.favorites.domain.usecase.CheckFavoriteStatusUseCase
import com.guicarneirodev.hoopreel.feature.favorites.domain.usecase.ToggleFavoriteUseCase
import com.guicarneirodev.hoopreel.feature.highlights.domain.repository.HighlightsRepository
import com.guicarneirodev.hoopreel.feature.player.domain.model.Video
import com.guicarneirodev.hoopreel.feature.player.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val repository: PlayerRepository,
    private val highlightsRepository: HighlightsRepository,
    private val checkFavoriteStatusUseCase: CheckFavoriteStatusUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<PlayerUiState>(PlayerUiState.Initial)
    val uiState = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    fun loadVideo(videoId: String) {
        viewModelScope.launch {
            _uiState.value = PlayerUiState.Loading
            try {
                val video = repository.getVideoDetails(videoId)
                _uiState.value = PlayerUiState.Success(video)

                checkFavoriteStatusUseCase(videoId).collect { isFav ->
                    _isFavorite.value = isFav
                }
            } catch (e: Exception) {
                _uiState.value = PlayerUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getPlayerName(videoId: String): String {
        return highlightsRepository.getPlayerByVideoId(videoId)?.name ?: ""
    }

    fun toggleFavorite() {
        val currentState = uiState.value
        if (currentState is PlayerUiState.Success) {
            val playerVideo = currentState.video

            val favoritesVideo = com.guicarneirodev.hoopreel.feature.favorites.domain.model.Video(
                id = playerVideo.id,
                title = playerVideo.title,
                thumbnailUrl = playerVideo.thumbnailUrl,
                videoUrl = playerVideo.videoUrl,
                statistics = playerVideo.statistics
            )

            viewModelScope.launch {
                toggleFavoriteUseCase(
                    video = favoritesVideo,
                    playerName = getPlayerName(playerVideo.id),
                    isFavorite = _isFavorite.value
                )
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