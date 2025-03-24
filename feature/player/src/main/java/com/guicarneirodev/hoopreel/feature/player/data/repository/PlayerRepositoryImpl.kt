package com.guicarneirodev.hoopreel.feature.player.data.repository

import android.util.Log
import com.guicarneirodev.hoopreel.core.network.youtube.YouTubeApiService
import com.guicarneirodev.hoopreel.core.network.youtube.model.getVideoId
import com.guicarneirodev.hoopreel.feature.player.domain.model.Video
import com.guicarneirodev.hoopreel.feature.player.domain.repository.PlayerRepository

class PlayerRepositoryImpl(
    private val youTubeApiService: YouTubeApiService
) : PlayerRepository {
    override suspend fun getVideoDetails(videoId: String): Video {
        return try {
            Log.d("PlayerRepo", "Fetching video details for id: $videoId")

            if (videoId.isBlank()) {
                throw IllegalArgumentException("Video ID cannot be empty")
            }

            val response = youTubeApiService.getVideos(id = videoId)
            Log.d("PlayerRepo", "Response received: $response")

            response.items.firstOrNull()?.let { videoItem ->
                Log.d("PlayerRepo", "Video item: $videoItem")

                Video(
                    id = videoItem.getVideoId(),
                    title = videoItem.snippet.title,
                    thumbnailUrl = videoItem.snippet.thumbnails.high.url,
                    videoUrl = "https://www.youtube.com/watch?v=$videoId"
                )
            } ?: throw IllegalStateException("Video not found")
        } catch (e: Exception) {
            Log.e("PlayerRepo", "Error fetching video details", e)
            throw e
        }
    }
}