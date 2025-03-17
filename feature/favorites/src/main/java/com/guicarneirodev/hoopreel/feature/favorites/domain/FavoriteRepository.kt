package com.guicarneirodev.hoopreel.feature.favorites.domain

import com.guicarneirodev.hoopreel.core.database.dao.FavoriteDao
import com.guicarneirodev.hoopreel.core.database.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import com.guicarneirodev.hoopreel.feature.player.domain.model.Video

class FavoriteRepository(private val favoriteDao: FavoriteDao) {

    fun getAllFavorites(): Flow<List<FavoriteEntity>> {
        return favoriteDao.getAllFavorites()
    }

    fun isFavorite(videoId: String): Flow<Boolean> {
        return favoriteDao.isFavorite(videoId)
    }

    suspend fun addFavorite(video: Video, playerName: String) {
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

    suspend fun removeFavorite(videoId: String) {
        favoriteDao.deleteFavoriteById(videoId)
    }

    suspend fun toggleFavorite(video: Video, playerName: String, isFavorite: Boolean) {
        if (isFavorite) {
            removeFavorite(video.id)
        } else {
            addFavorite(video, playerName)
        }
    }
}