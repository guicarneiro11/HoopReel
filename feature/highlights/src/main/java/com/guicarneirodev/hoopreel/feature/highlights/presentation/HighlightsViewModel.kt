package com.guicarneirodev.hoopreel.feature.highlights.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guicarneirodev.hoopreel.feature.highlights.domain.model.Player
import com.guicarneirodev.hoopreel.feature.highlights.domain.repository.HighlightsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HighlightsViewModel(
    private val repository: HighlightsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<HighlightsUiState>(HighlightsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    val players: StateFlow<List<Player>> = repository.observeAllPlayers()
        .catch { emit(emptyList()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            // Observando mudanças no players e atualizando o uiState
            players.collect { playerList ->
                if (playerList.isEmpty()) {
                    // Se não tiver dados em cache, carregar da API
                    if (_uiState.value !is HighlightsUiState.Loading) {
                        loadPlayers()
                    }
                } else {
                    _uiState.value = HighlightsUiState.Success(playerList)
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                repository.refreshData()
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
                if (this@HighlightsViewModel.players.value.isEmpty()) {
                    _uiState.value = HighlightsUiState.Success(players)
                }
            } catch (e: Exception) {
                _uiState.value = HighlightsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadPlayerDetails(playerId: String) {
        val currentState = _uiState.value
        if (currentState is HighlightsUiState.Success) {
            val playerExists = currentState.players.any { it.id == playerId }

            if (playerExists) {
                return
            }
        }

        viewModelScope.launch {
            _uiState.value = HighlightsUiState.Loading
            try {
                val playerHighlights = repository.getPlayerHighlights(playerId)

                if (currentState is HighlightsUiState.Success) {
                    val updatedPlayers = currentState.players.toMutableList()
                    val playerIndex = updatedPlayers.indexOfFirst { it.id == playerId }

                    if (playerIndex >= 0) {
                        updatedPlayers[playerIndex] = updatedPlayers[playerIndex].copy(
                            highlights = playerHighlights
                        )
                    } else {
                        val players = repository.getPlayers()
                        _uiState.value = HighlightsUiState.Success(players)
                        return@launch
                    }

                    _uiState.value = HighlightsUiState.Success(updatedPlayers)
                } else {
                    val players = repository.getPlayers()
                    _uiState.value = HighlightsUiState.Success(players)
                }
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