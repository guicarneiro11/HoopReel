package com.guicarneirodev.hoopreel.core.network.youtube.model

data class VideoItem(
    val id: String,
    val snippet: VideoSnippet,
    val statistics: VideoStatistics
)
