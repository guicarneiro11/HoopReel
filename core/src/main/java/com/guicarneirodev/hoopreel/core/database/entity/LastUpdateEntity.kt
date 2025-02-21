package com.guicarneirodev.hoopreel.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "last_update")
data class LastUpdateEntity(
    @PrimaryKey val id: Int = 1,
    val timestamp: Long
)