package com.guicarneirodev.hoopreel.feature.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guicarneirodev.hoopreel.feature.favorites.domain.usecase.GetFavoritesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoriteUiState>(FavoriteUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = FavoriteUiState.Loading

            getFavoritesUseCase()
                .catch { error ->
                    _uiState.value = FavoriteUiState.Error(error.message ?: "Erro desconhecido ao carregar favoritos")
                }
                .collect { favorites ->
                    if (favorites.isEmpty()) {
                        _uiState.value = FavoriteUiState.Empty
                    } else {
                        _uiState.value = FavoriteUiState.Success(favorites)
                    }
                }
        }
    }
}

sealed class FavoriteUiState {
    data object Loading : FavoriteUiState()
    data object Empty : FavoriteUiState()
    data class Success(val favorites: List<GetFavoritesUseCase.FavoriteVideo>) : FavoriteUiState()
    data class Error(val message: String) : FavoriteUiState()
}