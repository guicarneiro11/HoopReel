package com.guicarneirodev.hoopreel.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.guicarneirodev.hoopreel.core.database.entity.HighlightEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HighlightDao {
    @Query("SELECT * FROM highlights WHERE playerId = :playerId")
    suspend fun getHighlightsForPlayer(playerId: String): List<HighlightEntity>

    @Query("SELECT * FROM highlights WHERE playerId = :playerId")
    fun getHighlightsFlow(playerId: String): Flow<List<HighlightEntity>>

    @Query("SELECT DISTINCT playerId FROM highlights")
    fun getAllPlayerIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(highlights: List<HighlightEntity>)

    @Query("DELETE FROM highlights WHERE playerId = :playerId")
    suspend fun deleteHighlightsForPlayer(playerId: String)

    @Query("DELETE FROM highlights")
    suspend fun deleteAllHighlights()
}