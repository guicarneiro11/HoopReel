package com.guicarneirodev.hoopreel.feature.search.domain.usecase

import com.guicarneirodev.hoopreel.feature.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class GetRecentSearchesUseCase(private val repository: SearchRepository) {

    operator fun invoke(): Flow<List<String>> {
        return repository.getRecentSearches()
    }
}