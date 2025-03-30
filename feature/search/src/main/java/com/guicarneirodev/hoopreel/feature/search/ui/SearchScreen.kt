package com.guicarneirodev.hoopreel.feature.search.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guicarneirodev.hoopreel.feature.search.presentation.SearchUiState
import com.guicarneirodev.hoopreel.feature.search.presentation.SearchViewModel
import com.guicarneirodev.hoopreel.feature.search.ui.components.SearchBar
import com.guicarneirodev.hoopreel.feature.search.ui.components.SearchLoading
import com.guicarneirodev.hoopreel.feature.search.ui.components.SearchError
import com.guicarneirodev.hoopreel.feature.search.ui.components.EmptySearchResults
import com.guicarneirodev.hoopreel.feature.search.ui.components.InitialSearchState
import com.guicarneirodev.hoopreel.feature.search.ui.components.SearchResults
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    onVideoClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val recentSearches by viewModel.recentSearches.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                onSearch = { viewModel.onSearchSubmit() },
                onClearQuery = { viewModel.clearSearchQuery() },
                modifier = Modifier.padding(16.dp)
            )

            when (val state = uiState) {
                is SearchUiState.Initial -> {
                    InitialSearchState(
                        recentSearches = recentSearches,
                        onRecentSearchClick = viewModel::onRecentSearchClicked
                    )
                }

                is SearchUiState.Loading -> {
                    SearchLoading()
                }

                is SearchUiState.Success -> {
                    SearchResults(
                        results = state.results,
                        onVideoClick = { videoId ->
                            if (videoId.isNotBlank()) {
                                onVideoClick(videoId)
                            } else {
                                Log.e("SearchScreen", "Attempted to open video with empty ID")
                            }
                        }
                    )
                }

                is SearchUiState.Empty -> {
                    EmptySearchResults(query = state.query)
                }

                is SearchUiState.Error -> {
                    SearchError(
                        message = state.message,
                        onRetry = viewModel::onSearchSubmit
                    )
                }
            }
        }
    }
}