package com.guicarneirodev.hoopreel.core.network.youtube.model

data class VideoSnippet(
    val title: String,
    val description: String,
    val thumbnails: Thumbnails,
    val publishedAt: String,
    val channelTitle: String
)
