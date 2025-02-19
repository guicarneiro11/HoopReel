package com.guicarneirodev.hoopreel.feature.highlights.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guicarneirodev.hoopreel.feature.highlights.domain.model.Player
import com.guicarneirodev.hoopreel.feature.highlights.domain.repository.HighlightsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HighlightsViewModel(
    private val repository: HighlightsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<HighlightsUiState>(HighlightsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        Log.d("HighlightsViewModel", "Initializing...") // Debug log
        loadPlayers()
    }

    private fun loadPlayers() {
        viewModelScope.launch {
            try {
                Log.d("HighlightsViewModel", "Loading players...") // Debug log
                val players = repository.getPlayers()
                Log.d("HighlightsViewModel", "Players loaded: ${players.size}") // Debug log
                _uiState.value = HighlightsUiState.Success(players)
            } catch (e: Exception) {
                Log.e("HighlightsViewModel", "Error loading players", e) // Debug log
                _uiState.value = HighlightsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class HighlightsUiState {
    data object Loading : HighlightsUiState()
    data class Success(val players: List<Player>) : HighlightsUiState()
    data class Error(val message: String) : HighlightsUiState()
}