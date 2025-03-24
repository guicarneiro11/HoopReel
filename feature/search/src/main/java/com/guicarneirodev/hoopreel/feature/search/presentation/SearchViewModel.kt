package com.guicarneirodev.hoopreel.feature.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guicarneirodev.hoopreel.feature.search.domain.model.SearchResult
import com.guicarneirodev.hoopreel.feature.search.domain.usecase.GetRecentSearchesUseCase
import com.guicarneirodev.hoopreel.feature.search.domain.usecase.SearchVideosUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val searchVideosUseCase: SearchVideosUseCase,
    getRecentSearchesUseCase: GetRecentSearchesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Initial)
    val uiState = _uiState.asStateFlow()

    // Busca recentes
    val recentSearches: StateFlow<List<String>> = getRecentSearchesUseCase()
        .catch { emit(emptyList()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .flatMapLatest { query ->
                    flow {
                        if (query.isBlank()) {
                            emit(SearchUiState.Initial)
                        } else {
                            emit(SearchUiState.Loading)
                            try {
                                val results = searchVideosUseCase(query)
                                if (results.isEmpty()) {
                                    emit(SearchUiState.Empty(query))
                                } else {
                                    emit(SearchUiState.Success(results))
                                }
                            } catch (e: Exception) {
                                emit(SearchUiState.Error(e.message ?: "Erro ao buscar vídeos"))
                            }
                        }
                    }
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onSearchSubmit() {
        viewModelScope.launch {
            val query = _searchQuery.value
            if (query.isNotBlank()) {
                _uiState.value = SearchUiState.Loading
                try {
                    val results = searchVideosUseCase(query)
                    _uiState.value = if (results.isEmpty()) {
                        SearchUiState.Empty(query)
                    } else {
                        SearchUiState.Success(results)
                    }
                } catch (e: Exception) {
                    _uiState.value = SearchUiState.Error(e.message ?: "Erro ao buscar vídeos")
                }
            }
        }
    }

    fun onRecentSearchClicked(query: String) {
        _searchQuery.value = query
        onSearchSubmit()
    }

    fun clearSearchQuery() {
        _searchQuery.value = ""
        _uiState.value = SearchUiState.Initial
    }
}

sealed class SearchUiState {
    data object Initial : SearchUiState()
    data object Loading : SearchUiState()
    data class Success(val results: List<SearchResult>) : SearchUiState()
    data class Empty(val query: String) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}