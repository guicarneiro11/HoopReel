package com.guicarneirodev.hoopreel.feature.highlights.domain.repository

import com.guicarneirodev.hoopreel.feature.highlights.domain.model.Player
import com.guicarneirodev.hoopreel.feature.highlights.domain.model.VideoHighlight

interface HighlightsRepository {
    suspend fun getPlayerHighlights(playerId: String): List<VideoHighlight>
    suspend fun getPlayers(): List<Player>
    suspend fun refreshData()
    fun getPlayerByVideoId(videoId: String): Player?
}