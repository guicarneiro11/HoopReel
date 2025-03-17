package com.guicarneirodev.hoopreel.feature.favorites.domain.usecase

import com.guicarneirodev.hoopreel.core.network.youtube.model.VideoStatistics
import com.guicarneirodev.hoopreel.feature.favorites.domain.FavoriteRepository
import com.guicarneirodev.hoopreel.feature.player.domain.model.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFavoritesUseCase(private val favoriteRepository: FavoriteRepository) {

    operator fun invoke(): Flow<List<FavoriteVideo>> {
        return favoriteRepository.getAllFavorites().map { entities ->
            entities.map { entity ->
                FavoriteVideo(
                    video = Video(
                        id = entity.videoId,
                        title = entity.title,
                        thumbnailUrl = entity.thumbnailUrl,
                        videoUrl = "https://www.youtube.com/watch?v=${entity.videoId}",
                        statistics = VideoStatistics(
                            viewCount = entity.viewCount,
                            likeCount = entity.likeCount
                        )
                    ),
                    playerName = entity.playerName,
                    addedTimestamp = entity.addedTimestamp
                )
            }
        }
    }

    data class FavoriteVideo(
        val video: Video,
        val playerName: String,
        val addedTimestamp: Long
    )
}