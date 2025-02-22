package com.guicarneirodev.hoopreel.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.guicarneirodev.hoopreel.core.database.entity.LastUpdateEntity

@Dao
interface LastUpdateDao {
    @Query("SELECT * FROM last_update WHERE id = 1")
    suspend fun getLastUpdate(): LastUpdateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lastUpdate: LastUpdateEntity)

    @Query("DELETE FROM last_update")
    suspend fun clearLastUpdate()
}