package com.guicarneirodev.hoopreel.feature.highlights.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.guicarneirodev.hoopreel.feature.highlights.domain.repository.HighlightsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HighlightsUpdateWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {
    private val highlightsRepository: HighlightsRepository by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            Log.d(TAG, "Starting background update of highlights")

            highlightsRepository.refreshData()

            val players = highlightsRepository.getPlayers()

            Log.d(TAG, "Successfully updated highlights for ${players.size} players")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update highlights", e)
            if (e is java.io.IOException) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    companion object {
        private const val TAG = "HighlightsUpdateWorker"
    }
}