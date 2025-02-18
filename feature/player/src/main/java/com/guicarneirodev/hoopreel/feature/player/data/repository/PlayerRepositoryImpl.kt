package com.guicarneirodev.hoopreel.feature.player.data.repository

import com.guicarneirodev.hoopreel.core.network.youtube.YouTubeApiService
import com.guicarneirodev.hoopreel.feature.player.domain.model.Video
import com.guicarneirodev.hoopreel.feature.player.domain.repository.PlayerRepository

class PlayerRepositoryImpl (
    private val youTubeApiService: YouTubeApiService
) : PlayerRepository {
    override suspend fun getVideoDetails(videoId: String): Video {
        return youTubeApiService.getVideos(id = videoId)
            .items.firstOrNull()?.let { videoItem ->
                Video(
                    id = videoId,
                    title = videoItem.snippet.title,
                    thumbnailUrl = videoItem.snippet.thumbnails.high.url,
                    videoUrl = "https://www.youtube.com/watch?v=$videoId"
                )
            } ?: throw IllegalStateException("Video not found")
    }
}