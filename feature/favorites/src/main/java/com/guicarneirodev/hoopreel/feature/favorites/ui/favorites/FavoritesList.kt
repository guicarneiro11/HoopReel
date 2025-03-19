package com.guicarneirodev.hoopreel.feature.favorites.ui.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guicarneirodev.hoopreel.feature.favorites.domain.usecase.GetFavoritesUseCase.FavoriteVideo

@Composable
fun FavoritesList(
    favorites: List<FavoriteVideo>,
    onVideoClick: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Meus Favoritos",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(favorites) { favorite ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(initialAlpha = 0.3f) + slideInVertically(
                    initialOffsetY = { 50 },
                    animationSpec = tween(300)
                )
            ) {
                FavoriteVideoItem(
                    favoriteVideo = favorite,
                    onVideoClick = onVideoClick
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}