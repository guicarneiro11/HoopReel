package com.guicarneirodev.hoopreel.feature.favorites.data

import com.guicarneirodev.hoopreel.core.database.dao.FavoriteDao
import com.guicarneirodev.hoopreel.core.database.entity.FavoriteEntity
import com.guicarneirodev.hoopreel.feature.favorites.domain.FavoriteRepository
import com.guicarneirodev.hoopreel.feature.favorites.domain.model.Video
import kotlinx.coroutines.flow.Flow

class FavoriteRepositoryImpl(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository(favoriteDao) {

    override fun getAllFavorites(): Flow<List<FavoriteEntity>> {
        return favoriteDao.getAllFavorites()
    }

    override fun isFavorite(videoId: String): Flow<Boolean> {
        return favoriteDao.isFavorite(videoId)
    }

    override suspend fun addFavorite(video: Video, playerName: String) {
        val favoriteEntity = FavoriteEntity(
            videoId = video.id,
            playerName = playerName,
            title = video.title,
            thumbnailUrl = video.thumbnailUrl,
            viewCount = video.statistics?.viewCount,
            likeCount = video.statistics?.likeCount
        )
        favoriteDao.insertFavorite(favoriteEntity)
    }

    override suspend fun removeFavorite(videoId: String) {
        favoriteDao.deleteFavoriteById(videoId)
    }

    override suspend fun toggleFavorite(video: Video, playerName: String, isFavorite: Boolean) {
        if (isFavorite) {
            removeFavorite(video.id)
        } else {
            addFavorite(video, playerName)
        }
    }
}