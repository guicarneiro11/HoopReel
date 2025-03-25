package com.guicarneirodev.hoopreel.feature.highlights.data

import android.os.Build
import android.text.Html
import android.util.Log
import com.guicarneirodev.hoopreel.core.database.dao.HighlightDao
import com.guicarneirodev.hoopreel.core.database.dao.LastUpdateDao
import com.guicarneirodev.hoopreel.core.database.entity.HighlightEntity
import com.guicarneirodev.hoopreel.core.database.entity.LastUpdateEntity
import com.guicarneirodev.hoopreel.core.network.youtube.YouTubeApiService
import com.guicarneirodev.hoopreel.core.utils.PlayerImages
import com.guicarneirodev.hoopreel.core.utils.formatIsoDate
import com.guicarneirodev.hoopreel.feature.highlights.domain.model.Player
import com.guicarneirodev.hoopreel.feature.highlights.domain.model.VideoHighlight
import com.guicarneirodev.hoopreel.feature.highlights.domain.repository.HighlightsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
            searchTerms = "\"Lebron James\" mix",
            imageUrl = PlayerImages.LEBRON_JAMES
        ),
        Player(
            id = "kevin-durant",
            name = "Kevin Durant",
            searchTerms = "\"Kevin Durant\" mix",
            imageUrl = PlayerImages.KEVIN_DURANT
        ),
        Player(
            id = "steph-curry",
            name = "Stephen Curry",
            searchTerms = "\"Stephen Curry\" mix",
            imageUrl = PlayerImages.STEPHEN_CURRY
        ),
        Player(
            id = "luka-doncic",
            name = "Luka Dončić",
            searchTerms = "\"Luka Doncic\" highlights mix",
            imageUrl = PlayerImages.LUKA_DONCIC
        ),
        Player(
            id = "shai-gilgeous-alexander",
            name = "Shai Gilgeous-Alexander",
            searchTerms = "\"Shai Gilgeous-Alexander\" highlights mix",
            imageUrl = PlayerImages.SHAI_GILGEOUS_ALEXANDER
        ),
        Player(
            id = "jokic",
            name = "Nikola Jokić",
            searchTerms = "\"Nikola Jokic\" highlights mix",
            imageUrl = PlayerImages.NIKOLA_JOKIC
        ),
        Player(
            id = "giannis",
            name = "Giannis Antetokounmpo",
            searchTerms = "\"Giannis Antetokounmpo\" highlights mix",
            imageUrl = PlayerImages.GIANNIS_ANTETOKOUNMPO
        ),
        Player(
            id = "tatum",
            name = "Jayson Tatum",
            searchTerms = "\"Jayson Tatum\" highlights mix",
            imageUrl = PlayerImages.JAYSON_TATUM
        ),
        Player(
            id = "anthony-davis",
            name = "Anthony Davis",
            searchTerms = "\"Anthony Davis\" highlights mix",
            imageUrl = PlayerImages.ANTHONY_DAVIS
        ),
        Player(
            id = "trae-young",
            name = "Trae Young",
            searchTerms = "\"Trae Young\" highlights mix",
            imageUrl = PlayerImages.TRAE_YOUNG
        ),
        Player(
            id = "victor-wembanyama",
            name = "Victor Wembanyama",
            searchTerms = "\"Victor Wembanyama\" highlights mix",
            imageUrl = PlayerImages.VICTOR_WEMBANYAMA
        ),
        Player(
            id = "james-harden",
            name = "James Harden",
            searchTerms = "\"James Harden\" mix",
            imageUrl = PlayerImages.JAMES_HARDEN
        ),
        Player(
            id = "anthony-edwards",
            name = "Anthony Edwards",
            searchTerms = "\"Anthony Edwards\" highlights mix",
            imageUrl = PlayerImages.ANTHONY_EDWARDS
        ),
        Player(
            id = "russel-westbrook",
            name = "Russel Westbrook",
            searchTerms = "\"Russel Westbrook\" mix",
            imageUrl = PlayerImages.RUSSELL_WESTBROOK
        ),
        Player(
            id = "kyrie-irving",
            name = "Kyrie Irving",
            searchTerms = "\"Kyrie Irving\" mix",
            imageUrl = PlayerImages.KYRIE_IRVING
        ),
        Player(
            id = "ja-morant",
            name = "Ja Morant",
            searchTerms = "\"Ja Morant\" highlights mix",
            imageUrl = PlayerImages.JA_MORANT
        ),
        Player(
            id = "donovan-mitchell",
            name = "Donovan Mitchell",
            searchTerms = "\"Donovan Mitchell\" highlights mix",
            imageUrl = PlayerImages.DONOVAN_MITCHELL
        ),
        Player(
            id = "cade-cunningham",
            name = "Cade Cunningham",
            searchTerms = "\"Cade Cunningham\" highlights mix",
            imageUrl = PlayerImages.CADE_CUNNINGHAM
        ),
        Player(
            id = "joel-embiid",
            name = "Joel Embiid",
            searchTerms = "\"Joel Embiid\" highlights mix",
            imageUrl = PlayerImages.JOEL_EMBIID
        ),
        Player(
            id = "damian-lillard",
            name = "Damian Lillard",
            searchTerms = "\"Damian Lillard\" highlights mix",
            imageUrl = PlayerImages.DAMIAN_LILLARD
        ),
        Player(
            id = "devin-booker",
            name = "Devin Booker",
            searchTerms = "\"Devin Booker\" highlights mix",
            imageUrl = PlayerImages.DEVIN_BOOKER
        ),
        Player(
            id = "zion-williamson",
            name = "Zion Williamson",
            searchTerms = "\"Zion Williamson\" highlights mix",
            imageUrl = PlayerImages.ZION_WILLIAMSON
        ),
        Player(
            id = "lamelo-ball",
            name = "LaMelo Ball",
            searchTerms = "\"LaMelo Ball\" highlights mix",
            imageUrl = PlayerImages.LAMELO_BALL
        ),
        Player(
            id = "kawhi-leonard",
            name = "Kawhi Leonard",
            searchTerms = "\"Kawhi Leonard\" highlights mix",
            imageUrl = PlayerImages.KAWHI_LEONARD
        ),
        Player(
            id = "paolo-banchero",
            name = "Paolo Banchero",
            searchTerms = "\"Paolo Banchero\" highlights mix",
            imageUrl = PlayerImages.PAOLO_BANCHERO
        ),
        Player(
            id = "allen-iverson",
            name = "Allen Iverson",
            searchTerms = "\"Allen Iverson\" mix",
            imageUrl = PlayerImages.ALLEN_IVERSON
        ),
    )

    override suspend fun getPlayers(): List<Player> = coroutineScope {
        if (shouldUpdateCache()) {
            try {
                val deferredPlayers = players.map { player ->
                    async {
                        val highlights = getPlayerHighlightsFromYoutube(player.id, player.searchTerms)
                        // Salvar no cache
                        saveToCache(player.id, highlights)
                        player.copy(highlights = highlights)
                    }
                }
                deferredPlayers.awaitAll()
            } catch (e: Exception) {
                Log.e("HighlightsRepo", "Error fetching from API, using cache", e)
                players.map { player ->
                    player.copy(highlights = getFromCache(player.id))
                }
            }
        } else {
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
                title = entity.title.decodeHtml(),
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
            val response = youTubeApiService.searchVideos(
                query = searchTerms,
                maxResults = 20
            )

            response.items
                .mapNotNull { videoItem ->
                    val title = videoItem.snippet.title.decodeHtml().lowercase()
                    val playerName = players.find { it.id == playerId }?.name?.lowercase()
                        ?: return@mapNotNull null

                    val isRelevant = title.contains(playerName) &&
                            !players.any { otherPlayer ->
                                otherPlayer.id != playerId &&
                                        title.contains(otherPlayer.name.lowercase())
                            }

                    val videoId = videoItem.id.let { id ->
                        when (id) {
                            is Map<*, *> -> id["videoId"] as? String
                            is String -> id
                            else -> {
                                Log.e("HighlightsRepo", "Unknown id type: ${id::class.java}")
                                null
                            }
                        }
                    }

                    if (isRelevant && videoId != null) {
                        VideoHighlight(
                            id = videoId,
                            title = videoItem.snippet.title.decodeHtml(),
                            thumbnailUrl = videoItem.snippet.thumbnails.high.url,
                            views = "N/A",
                            publishedAt = videoItem.snippet.publishedAt
                        )
                    } else null
                }
                .take(10)
        } catch (e: Exception) {
            Log.e("HighlightsRepo", "Error fetching highlights for $playerId", e)
            emptyList()
        }
    }

    override suspend fun refreshData() {
        highlightDao.deleteAllHighlights()
        lastUpdateDao.clearLastUpdate()
    }

    override fun getPlayerByVideoId(videoId: String): Player? {
        return players.find { player ->
            player.highlights.any { highlight -> highlight.id == videoId }
        }
    }

    override fun observePlayerHighlights(playerId: String): Flow<List<VideoHighlight>> {
        return highlightDao.getHighlightsFlow(playerId)
            .map { entities ->
                entities.map { entity ->
                    VideoHighlight(
                        id = entity.id,
                        title = entity.title.decodeHtml(),
                        thumbnailUrl = entity.thumbnailUrl,
                        publishedAt = entity.publishedAt.formatIsoDate(),
                        views = "N/A"
                    )
                }
            }
    }

    override fun observeAllPlayers(): Flow<List<Player>> {
        return highlightDao.getAllPlayerIds().map { playerIds ->
            playerIds.mapNotNull { playerId ->
                players.find { it.id == playerId }?.let { playerTemplate ->
                    highlightDao.getHighlightsForPlayer(playerId).let { highlights ->
                        playerTemplate.copy(
                            highlights = highlights.map { entity ->
                                VideoHighlight(
                                    id = entity.id,
                                    title = entity.title.decodeHtml(),
                                    thumbnailUrl = entity.thumbnailUrl,
                                    publishedAt = entity.publishedAt.formatIsoDate(),
                                    views = "N/A"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

fun String.decodeHtml(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this).toString()
    }
}