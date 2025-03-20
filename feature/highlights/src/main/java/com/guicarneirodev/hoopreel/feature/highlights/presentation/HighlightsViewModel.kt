package com.guicarneirodev.hoopreel.feature.highlights.presentation

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

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        loadPlayers()
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                repository.refreshData()
                loadPlayers()
            } catch (e: Exception) {
                _uiState.value = HighlightsUiState.Error(e.message ?: "Unknown error")
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun loadPlayers() {
        viewModelScope.launch {
            _uiState.value = HighlightsUiState.Loading
            try {
                val players = repository.getPlayers()
                _uiState.value = HighlightsUiState.Success(players)
            } catch (e: Exception) {
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