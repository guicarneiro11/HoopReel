package com.guicarneirodev.hoopreel.feature.favorites.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guicarneirodev.hoopreel.feature.favorites.presentation.FavoriteUiState
import com.guicarneirodev.hoopreel.feature.favorites.presentation.FavoriteViewModel
import com.guicarneirodev.hoopreel.feature.favorites.ui.favorites.EmptyFavoritesView
import com.guicarneirodev.hoopreel.feature.favorites.ui.favorites.FavoritesList
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FavoritesScreen(
    viewModel: FavoriteViewModel = koinViewModel(),
    onVideoClick: (String) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (val state = uiState) {
            is FavoriteUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFFF8036)
                )
            }

            is FavoriteUiState.Empty -> {
                EmptyFavoritesView(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is FavoriteUiState.Success -> {
                FavoritesList(
                    favorites = state.favorites,
                    onVideoClick = onVideoClick
                )
            }

            is FavoriteUiState.Error -> {
                Text(
                    text = "Erro: ${state.message}",
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}