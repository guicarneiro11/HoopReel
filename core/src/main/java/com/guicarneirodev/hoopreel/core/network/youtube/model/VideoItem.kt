package com.guicarneirodev.hoopreel.core.network.youtube.model

import com.google.gson.annotations.SerializedName

data class VideoItem(
    @SerializedName("id")
    val id: Any,
    val snippet: VideoSnippet,
    val statistics: VideoStatistics? = null
)

fun VideoItem.getVideoId(): String {
    return when (id) {
        is String -> id as String
        is VideoId -> (id as VideoId).videoId ?: ""
        else -> ""
    }
}