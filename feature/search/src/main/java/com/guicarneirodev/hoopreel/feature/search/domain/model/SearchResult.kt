package com.guicarneirodev.hoopreel.feature.search.domain.model

data class SearchResult(
    val id: String,
    val title: String,
    val thumbnailUrl: String,
    val playerName: String?,
    val publishedAt: String
)