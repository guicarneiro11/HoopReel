package com.guicarneirodev.hoopreel.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.guicarneirodev.hoopreel.core.database.entity.HighlightEntity

@Dao
interface HighlightDao {
    @Query("SELECT * FROM highlights WHERE playerId = :playerId")
    suspend fun getHighlightsForPlayer(playerId: String): List<HighlightEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(highlights: List<HighlightEntity>)

    @Query("DELETE FROM highlights WHERE playerId = :playerId")
    suspend fun deleteHighlightsForPlayer(playerId: String)

    @Query("DELETE FROM highlights")
    suspend fun deleteAllHighlights()
}