package com.guicarneirodev.hoopreel.feature.player.domain.repository

import com.guicarneirodev.hoopreel.feature.player.domain.model.Video

interface PlayerRepository {
    suspend fun getVideoDetails(videoId: String): Video
}