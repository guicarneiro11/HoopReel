package com.guicarneirodev.hoopreel.feature.highlights.data

import android.util.Log
import com.guicarneirodev.hoopreel.core.network.youtube.YouTubeApiService
import com.guicarneirodev.hoopreel.feature.highlights.domain.model.Player
import com.guicarneirodev.hoopreel.feature.highlights.domain.model.VideoHighlight
import com.guicarneirodev.hoopreel.feature.highlights.domain.repository.HighlightsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class HighlightsRepositoryImpl(
    private val youTubeApiService: YouTubeApiService
) : HighlightsRepository {
    private val players = listOf(
        Player(
            id = "steph-curry",
            name = "Stephen Curry",
            searchTerms = "stephen curry greatest highlights warriors",
            imageUrl = "..."
        ),
        Player(
            id = "kyrie-irving",
            name = "Kyrie Irving",
            searchTerms = "kyrie irving best handles highlights",
            imageUrl = "..."
        ),
        Player(
            id = "ja-morant",
            name = "Ja Morant",
            searchTerms = "ja morant dunk highlights grizzlies",
            imageUrl = "..."
        ),

        Player(
            id = "lebron-james",
            name = "LeBron James",
            searchTerms = "lebron james best plays career highlights",
            imageUrl = "..."
        ),
        Player(
            id = "kevin-durant",
            name = "Kevin Durant",
            searchTerms = "kevin durant scoring highlights",
            imageUrl = "..."
        ),
        Player(
            id = "giannis",
            name = "Giannis Antetokounmpo",
            searchTerms = "giannis antetokounmpo best plays bucks",
            imageUrl = "..."
        ),

        Player(
            id = "luka-doncic",
            name = "Luka Dončić",
            searchTerms = "luka doncic magic highlights mavericks",
            imageUrl = "..."
        ),
        Player(
            id = "jokic",
            name = "Nikola Jokić",
            searchTerms = "nikola jokic passing highlights nuggets",
            imageUrl = "..."
        ),
        Player(
            id = "tatum",
            name = "Jayson Tatum",
            searchTerms = "jayson tatum celtics highlights",
            imageUrl = "..."
        )
    )

    override suspend fun getPlayers(): List<Player> {
        return coroutineScope {
            val deferredPlayers = players.map { player ->
                async {
                    player.copy(
                        highlights = getPlayerHighlightsFromYoutube(player.id, player.searchTerms)
                    )
                }
            }
            deferredPlayers.awaitAll()
        }
    }

    override suspend fun getPlayerHighlights(playerId: String): List<VideoHighlight> {
        val player = players.find { it.id == playerId }
            ?: throw IllegalArgumentException("Player not found: $playerId")

        return getPlayerHighlightsFromYoutube(playerId, player.searchTerms)
    }

    private suspend fun getPlayerHighlightsFromYoutube(
        playerId: String,
        searchTerms: String
    ): List<VideoHighlight> {
        return try {
            Log.d("HighlightsRepo", "Fetching highlights for player: $playerId with terms: $searchTerms")

            val response = youTubeApiService.searchVideos(
                query = searchTerms,
                maxResults = 10
            )

            // Adicionar log da resposta bruta
            Log.d("HighlightsRepo", "Raw response items: ${response.items}")

            response.items.mapNotNull { videoItem ->
                // Adicionar log para cada item
                Log.d("HighlightsRepo", "Processing video item: $videoItem")

                val videoId = videoItem.id?.let { id ->
                    when (id) {
                        is Map<*, *> -> id["videoId"] as? String
                        is String -> id
                        else -> {
                            Log.e("HighlightsRepo", "Unknown id type: ${id::class.java}")
                            null
                        }
                    }
                }

                videoId?.let { id ->
                    VideoHighlight(
                        id = id,
                        title = videoItem.snippet.title,
                        thumbnailUrl = videoItem.snippet.thumbnails.high.url,
                        views = "N/A",
                        publishedAt = videoItem.snippet.publishedAt
                    ).also {
                        Log.d("HighlightsRepo", "Created highlight: $it")
                    }
                }
            }.also { highlights ->
                Log.d("HighlightsRepo", "Found ${highlights.size} highlights for $playerId")
            }
        } catch (e: Exception) {
            Log.e("HighlightsRepo", "Error fetching highlights for $playerId", e)
            Log.e("HighlightsRepo", "Stack trace: ${e.stackTraceToString()}")
            emptyList()
        }
    }
}