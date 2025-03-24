package com.guicarneirodev.hoopreel.feature.search.domain.repository

import com.guicarneirodev.hoopreel.feature.search.domain.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchVideos(query: String): List<SearchResult>
    fun getRecentSearches(): Flow<List<String>>
    suspend fun saveRecentSearch(query: String)
    suspend fun clearRecentSearches()
}