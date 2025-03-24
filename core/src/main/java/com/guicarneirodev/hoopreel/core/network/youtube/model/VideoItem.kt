package com.guicarneirodev.hoopreel.core.network.youtube.model

import android.util.Log
import com.google.gson.annotations.SerializedName

data class VideoItem(
    @SerializedName("id")
    val id: Any,
    val snippet: VideoSnippet,
    val statistics: VideoStatistics? = null
)

fun VideoItem.getVideoId(): String {
    return when (id) {
        is String -> id
        is VideoId -> id.videoId ?: ""
        is Map<*, *> -> id["videoId"] as? String ?: ""
        else -> {
            Log.e("VideoItem", "Unknown id type: ${id::class.java}")
            ""
        }
    }
}