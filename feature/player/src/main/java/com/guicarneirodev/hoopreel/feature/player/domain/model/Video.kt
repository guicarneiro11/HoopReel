package com.guicarneirodev.hoopreel.feature.player.domain.model

import com.guicarneirodev.hoopreel.core.network.youtube.model.VideoStatistics

data class Video(
    val id: String,
    val title: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val statistics: VideoStatistics? = null
)