package com.guicarneirodev.hoopreel.feature.favorites.domain.usecase

import com.guicarneirodev.hoopreel.feature.favorites.domain.FavoriteRepository
import kotlinx.coroutines.flow.Flow

class CheckFavoriteStatusUseCase(private val favoriteRepository: FavoriteRepository) {

    operator fun invoke(videoId: String): Flow<Boolean> {
        return favoriteRepository.isFavorite(videoId)
    }
}