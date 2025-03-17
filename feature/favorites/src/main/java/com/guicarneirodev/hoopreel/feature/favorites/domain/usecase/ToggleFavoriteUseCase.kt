package com.guicarneirodev.hoopreel.feature.favorites.domain.usecase

import com.guicarneirodev.hoopreel.feature.favorites.domain.FavoriteRepository
import com.guicarneirodev.hoopreel.feature.player.domain.model.Video


class ToggleFavoriteUseCase(private val favoriteRepository: FavoriteRepository) {

    suspend operator fun invoke(video: Video, playerName: String, isFavorite: Boolean) {
        favoriteRepository.toggleFavorite(video, playerName, isFavorite)
    }
}