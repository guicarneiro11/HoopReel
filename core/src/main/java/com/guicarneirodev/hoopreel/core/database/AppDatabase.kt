package com.guicarneirodev.hoopreel.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.guicarneirodev.hoopreel.core.database.dao.HighlightDao
import com.guicarneirodev.hoopreel.core.database.dao.LastUpdateDao
import com.guicarneirodev.hoopreel.core.database.entity.HighlightEntity
import com.guicarneirodev.hoopreel.core.database.entity.LastUpdateEntity

@Database(
    entities = [HighlightEntity::class, LastUpdateEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun highlightDao(): HighlightDao
    abstract fun lastUpdateDao(): LastUpdateDao

    companion object {
        const val DATABASE_NAME = "hoopreel_db"
    }
}