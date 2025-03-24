package com.guicarneirodev.hoopreel.feature.search.domain.usecase

import com.guicarneirodev.hoopreel.feature.search.domain.model.SearchResult
import com.guicarneirodev.hoopreel.feature.search.domain.repository.SearchRepository

class SearchVideosUseCase(private val repository: SearchRepository) {

    suspend operator fun invoke(query: String): List<SearchResult> {
        return if (query.isBlank()) {
            emptyList()
        } else {
            repository.saveRecentSearch(query)
            repository.searchVideos(query)
        }
    }
}