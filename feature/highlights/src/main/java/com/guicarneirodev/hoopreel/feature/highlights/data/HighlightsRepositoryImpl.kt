package com.guicarneirodev.hoopreel.feature.highlights.data

import android.util.Log
import com.guicarneirodev.hoopreel.core.database.dao.HighlightDao
import com.guicarneirodev.hoopreel.core.database.dao.LastUpdateDao
import com.guicarneirodev.hoopreel.core.database.entity.HighlightEntity
import com.guicarneirodev.hoopreel.core.database.entity.LastUpdateEntity
import com.guicarneirodev.hoopreel.core.network.youtube.YouTubeApiService
import com.guicarneirodev.hoopreel.feature.highlights.domain.model.Player
import com.guicarneirodev.hoopreel.feature.highlights.domain.model.VideoHighlight
import com.guicarneirodev.hoopreel.feature.highlights.domain.repository.HighlightsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.TimeUnit

class HighlightsRepositoryImpl(
    private val youTubeApiService: YouTubeApiService,
    private val highlightDao: HighlightDao,
    private val lastUpdateDao: LastUpdateDao
) : HighlightsRepository {

    private val CACHE_DURATION = TimeUnit.HOURS.toMillis(24)

    private val players = listOf(
        Player(
            id = "lebron-james",
            name = "LeBron James",
            searchTerms = "Lebron James mix",
            imageUrl = "..."
        ),
        Player(
            id = "kevin-durant",
            name = "Kevin Durant",
            searchTerms = "Kevin Durant mix",
            imageUrl = "..."
        ),
        Player(
            id = "steph-curry",
            name = "Stephen Curry",
            searchTerms = "Stephen Curry mix",
            imageUrl = "..."
        ),
        Player(
            id = "luka-doncic",
            name = "Luka Dončić",
            searchTerms = "Luka Doncic mix",
            imageUrl = "..."
        ),
        Player(
            id = "shai-gilgeous-alexander",
            name = "Shai Gilgeous-Alexander",
            searchTerms = "Shai Gilgeous-Alexander mix",
            imageUrl = "..."
        ),
        Player(
            id = "jokic",
            name = "Nikola Jokić",
            searchTerms = "Nikola Jokic mix",
            imageUrl = "..."
        ),
        Player(
            id = "giannis",
            name = "Giannis Antetokounmpo",
            searchTerms = "Giannis Antetokounmpo mix",
            imageUrl = "..."
        ),
        Player(
            id = "tatum",
            name = "Jayson Tatum",
            searchTerms = "Jayson Tatum mix",
            imageUrl = "..."
        ),
        Player(
            id = "anthony-davis",
            name = "Anthony Davis",
            searchTerms = "Anthony Davis mix",
            imageUrl = "..."
        ),
        Player(
            id = "trae-young",
            name = "Trae Young",
            searchTerms = "Trae Young mix",
            imageUrl = "..."
        ),
        Player(
            id = "victor-wembanyama",
            name = "Victor Wembanyama",
            searchTerms = "Victor Wembanyama mix",
            imageUrl = "..."
        ),
        Player(
            id = "james-harden",
            name = "James Harden",
            searchTerms = "James Harden mix",
            imageUrl = "..."
        ),
        Player(
            id = "anthony-edwards",
            name = "Anthony Edwards",
            searchTerms = "Anthony Edwards mix",
            imageUrl = "..."
        ),
        Player(
            id = "russel-westbrook",
            name = "Russel Westbrook",
            searchTerms = "Russel Westbrook mix",
            imageUrl = "..."
        ),
        Player(
            id = "kyrie-irving",
            name = "Kyrie Irving",
            searchTerms = "Kyrie Irving mix",
            imageUrl = "..."
        ),
        Player(
            id = "ja-morant",
            name = "Ja Morant",
            searchTerms = "Ja Morant mix",
            imageUrl = "..."
        ),
        Player(
            id = "donovan-mitchell",
            name = "Donovan Mitchell",
            searchTerms = "Donovan Mitchell mix",
            imageUrl = "..."
        ),
        Player(
            id = "cade-cunningham",
            name = "Cade Cunningham",
            searchTerms = "Cade Cunningham mix",
            imageUrl = "..."
        ),
        Player(
            id = "joel-embiid",
            name = "Joel Embiid",
            searchTerms = "Joel Embiid mix",
            imageUrl = "..."
        ),
        Player(
            id = "damian-lillard",
            name = "Damian Lillard",
            searchTerms = "Damian Lillard mix",
            imageUrl = "..."
        ),
        Player(
            id = "devin-booker",
            name = "Devin Booker",
            searchTerms = "Devin Booker mix",
            imageUrl = "..."
        ),
        Player(
            id = "zion-williamson",
            name = "Zion Williamson",
            searchTerms = "Zion Williamson mix",
            imageUrl = "..."
        ),
        Player(
            id = "lamelo-ball",
            name = "LaMelo Ball",
            searchTerms = "LaMelo Ball mix",
            imageUrl = "..."
        ),
        Player(
            id = "kawhi-leonard",
            name = "Kawhi Leonard",
            searchTerms = "Kawhi Leonard mix",
            imageUrl = "..."
        ),
        Player(
            id = "paolo-banchero",
            name = "Paolo Banchero",
            searchTerms = "Paolo Banchero mix",
            imageUrl = "..."
        ),
        Player(
            id = "allen-iverson",
            name = "Allen Iverson",
            searchTerms = "allen-iverson mix",
            imageUrl = "..."
        ),
    )

    override suspend fun getPlayers(): List<Player> = coroutineScope {
        // Verificar se precisa atualizar o cache
        if (shouldUpdateCache()) {
            try {
                // Buscar highlights de todos os jogadores em paralelo
                val deferredPlayers = players.map { player ->
                    async {
                        val highlights = getPlayerHighlightsFromYoutube(player.id, player.searchTerms)
                        // Salvar no cache
                        saveToCache(player.id, highlights)
                        player.copy(highlights = highlights)
                    }
                }
                // Aguardar todos os resultados
                deferredPlayers.awaitAll()
            } catch (e: Exception) {
                Log.e("HighlightsRepo", "Error fetching from API, using cache", e)
                // Em caso de erro, usar cache para todos os jogadores
                players.map { player ->
                    player.copy(highlights = getFromCache(player.id))
                }
            }
        } else {
            // Usar cache para todos os jogadores
            players.map { player ->
                player.copy(highlights = getFromCache(player.id))
            }
        }
    }

    override suspend fun getPlayerHighlights(playerId: String): List<VideoHighlight> {
        val player = players.find { it.id == playerId }
            ?: throw IllegalArgumentException("Player not found: $playerId")

        return if (shouldUpdateCache()) {
            try {
                val highlights = getPlayerHighlightsFromYoutube(playerId, player.searchTerms)
                saveToCache(playerId, highlights)
                highlights
            } catch (e: Exception) {
                Log.e("HighlightsRepo", "Error fetching from API, using cache", e)
                getFromCache(playerId)
            }
        } else {
            getFromCache(playerId)
        }
    }

    private suspend fun shouldUpdateCache(): Boolean {
        val lastUpdate = lastUpdateDao.getLastUpdate()
        return lastUpdate == null ||
                System.currentTimeMillis() - lastUpdate.timestamp > CACHE_DURATION
    }

    private suspend fun saveToCache(playerId: String, highlights: List<VideoHighlight>) {
        val entities = highlights.map { highlight ->
            HighlightEntity(
                id = highlight.id,
                playerId = playerId,
                title = highlight.title,
                thumbnailUrl = highlight.thumbnailUrl,
                publishedAt = highlight.publishedAt,
                cached_at = System.currentTimeMillis()
            )
        }

        highlightDao.deleteHighlightsForPlayer(playerId)
        highlightDao.insertAll(entities)
        lastUpdateDao.insert(LastUpdateEntity(timestamp = System.currentTimeMillis()))
    }

    private suspend fun getFromCache(playerId: String): List<VideoHighlight> {
        return highlightDao.getHighlightsForPlayer(playerId).map { entity ->
            VideoHighlight(
                id = entity.id,
                title = entity.title,
                thumbnailUrl = entity.thumbnailUrl,
                publishedAt = entity.publishedAt,
                views = "N/A"
            )
        }
    }

    private suspend fun getPlayerHighlightsFromYoutube(
        playerId: String,
        searchTerms: String
    ): List<VideoHighlight> {
        return try {
            Log.d("HighlightsRepo", "Fetching highlights for player: $playerId")

            val response = youTubeApiService.searchVideos(
                query = searchTerms,
                maxResults = 10
            )

            response.items.mapNotNull { videoItem ->
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
                    )
                }
            }.also { highlights ->
                Log.d("HighlightsRepo", "Found ${highlights.size} highlights for $playerId")
            }
        } catch (e: Exception) {
            Log.e("HighlightsRepo", "Error fetching highlights for $playerId", e)
            throw e // Propagar erro para usar cache
        }
    }
}