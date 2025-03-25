package com.guicarneirodev.hoopreel.feature.highlights.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.guicarneirodev.hoopreel.core.database.dao.FavoriteDao
import com.guicarneirodev.hoopreel.core.database.entity.FavoriteEntity
import com.guicarneirodev.hoopreel.feature.highlights.workers.HighlightsUpdateWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import java.util.concurrent.TimeUnit

class StatisticsViewModel(
    favoriteDao: FavoriteDao,
    private val workManager: WorkManager
) : ViewModel() {

    val favoritesCount: StateFlow<Int> = favoriteDao.getFavoritesCount()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val recentFavorites: StateFlow<List<FavoriteEntity>> = favoriteDao
        .getFavoritesSince(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7))
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isUpdateRunning = MutableStateFlow(false)
    val isUpdateRunning = _isUpdateRunning.asStateFlow()

    fun runUpdateNow() {
        _isUpdateRunning.value = true

        val updateRequest = OneTimeWorkRequestBuilder<HighlightsUpdateWorker>()
            .addTag("manual_update")
            .build()

        workManager.enqueueUniqueWork(
            "manual_update",
            ExistingWorkPolicy.REPLACE,
            updateRequest
        )

        // Observar o estado do trabalho
        workManager.getWorkInfosByTagLiveData("manual_update")
            .asFlow()
            .onEach { workInfoList ->
                val isFinished = workInfoList.all { workInfo ->
                    workInfo.state == WorkInfo.State.SUCCEEDED ||
                            workInfo.state == WorkInfo.State.FAILED ||
                            workInfo.state == WorkInfo.State.CANCELLED
                }

                if (isFinished && workInfoList.isNotEmpty()) {
                    _isUpdateRunning.value = false
                }
            }
            .launchIn(viewModelScope)
    }
}