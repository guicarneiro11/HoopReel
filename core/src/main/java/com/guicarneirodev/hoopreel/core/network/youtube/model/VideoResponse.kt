package com.guicarneirodev.hoopreel.core.network.youtube.model

data class VideoResponse(
    val items: List<VideoItem>
)

data class VideoItem(
    val id: String,
    val snippet: VideoSnippet,
    val statistics: VideoStatistics
)

data class VideoSnippet(
    val title: String,
    val description: String,
    val thumbnails: Thumbnails,
    val publishedAt: String,
    val channelTitle: String
)

data class Thumbnails(
    val default: ThumbnailItem,
    val medium: ThumbnailItem,
    val high: ThumbnailItem
)

data class ThumbnailItem(
    val url: String,
    val width: Int,
    val height: Int
)

data class VideoStatistics(
    val viewCount: String,
    val likeCount: String
)