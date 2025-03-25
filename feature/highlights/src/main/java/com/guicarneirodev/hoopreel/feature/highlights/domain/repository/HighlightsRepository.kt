package com.guicarneirodev.hoopreel.feature.highlights.domain.repository

import com.guicarneirodev.hoopreel.feature.highlights.domain.model.Player
import com.guicarneirodev.hoopreel.feature.highlights.domain.model.VideoHighlight
import kotlinx.coroutines.flow.Flow

interface HighlightsRepository {
    suspend fun getPlayerHighlights(playerId: String): List<VideoHighlight>
    suspend fun getPlayers(): List<Player>
    suspend fun refreshData()
    fun getPlayerByVideoId(videoId: String): Player?
    fun observePlayerHighlights(playerId: String): Flow<List<VideoHighlight>>
    fun observeAllPlayers(): Flow<List<Player>>
}