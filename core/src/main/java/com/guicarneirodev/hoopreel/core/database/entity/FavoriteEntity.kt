package com.guicarneirodev.hoopreel.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val videoId: String,
    val playerName: String,
    val title: String,
    val thumbnailUrl: String,
    val viewCount: String? = null,
    val likeCount: String? = null,
    val addedTimestamp: Long = System.currentTimeMillis()
)