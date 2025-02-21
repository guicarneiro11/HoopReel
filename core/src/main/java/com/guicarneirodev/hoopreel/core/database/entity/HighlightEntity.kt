package com.guicarneirodev.hoopreel.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "highlights")
data class HighlightEntity (
    @PrimaryKey val id: String,
    val playerId: String,
    val title: String,
    val thumbnailUrl: String,
    val publishedAt: String,
    val cached_at: Long
)