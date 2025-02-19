package com.guicarneirodev.hoopreel.feature.highlights.domain.model

data class Player(
    val id: String,
    val name: String,
    val searchTerms: String,
    val imageUrl: String,
    val highlights: List<VideoHighlight> = emptyList()
)