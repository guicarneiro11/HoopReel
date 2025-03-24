package com.guicarneirodev.hoopreel.feature.search.data

import android.text.Html
import android.util.Log
import com.guicarneirodev.hoopreel.core.network.youtube.YouTubeApiService
import com.guicarneirodev.hoopreel.core.network.youtube.model.getVideoId
import com.guicarneirodev.hoopreel.feature.highlights.domain.repository.HighlightsRepository
import com.guicarneirodev.hoopreel.feature.search.domain.model.SearchResult
import com.guicarneirodev.hoopreel.feature.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.ConcurrentLinkedQueue

class SearchRepositoryImpl(
    private val youTubeApiService: YouTubeApiService,
    private val highlightsRepository: HighlightsRepository
) : SearchRepository {

    private val recentSearches = ConcurrentLinkedQueue<String>()
    private val maxRecentSearches = 10

    private fun String.decodeHtml(): String {
        return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    }

    override suspend fun searchVideos(query: String): List<SearchResult> {
        return try {
            val response = youTubeApiService.searchVideos(
                query = "NBA $query highlights",
                maxResults = 20
            )

            response.items.map { item ->
                val videoId = item.getVideoId()
                val title = item.snippet.title.decodeHtml()
                val thumbnailUrl = item.snippet.thumbnails.high.url
                val publishedAt = item.snippet.publishedAt

                val player = highlightsRepository.getPlayerByVideoId(videoId)
                val playerName = player?.name

                SearchResult(
                    id = videoId,
                    title = title,
                    thumbnailUrl = thumbnailUrl,
                    playerName = playerName,
                    publishedAt = publishedAt
                )
            }
        } catch (e: Exception) {
            Log.e("SearchRepo", "Error searching videos", e)
            emptyList()
        }
    }

    override fun getRecentSearches(): Flow<List<String>> = flow {
        emit(recentSearches.toList())
    }

    override suspend fun saveRecentSearch(query: String) {
        if (query.isBlank()) return

        recentSearches.remove(query)

        recentSearches.add(query)

        while (recentSearches.size > maxRecentSearches) {
            recentSearches.poll()
        }
    }

    override suspend fun clearRecentSearches() {
        recentSearches.clear()
    }
}