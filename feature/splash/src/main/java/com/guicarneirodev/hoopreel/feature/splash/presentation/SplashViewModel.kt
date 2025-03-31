package com.guicarneirodev.hoopreel.feature.splash.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guicarneirodev.hoopreel.feature.highlights.domain.repository.HighlightsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel(
    private val highlightsRepository: HighlightsRepository
) : ViewModel() {

    private val _progress = mutableFloatStateOf(0f)
    val progress: State<Float> = _progress

    private val _isLoadingComplete = mutableStateOf(false)
    val isLoadingComplete: State<Boolean> = _isLoadingComplete

    fun startLoading() {
        viewModelScope.launch {
            val progressJob = launch {
                simulateProgressIncrease()
            }

            val dataLoadJob = launch {
                preloadData()
            }

            dataLoadJob.join()

            while (_progress.floatValue < 1f) {
                _progress.floatValue = minOf(_progress.floatValue + 0.05f, 1f)
                delay(50)
            }

            progressJob.cancel()

            _isLoadingComplete.value = true
        }
    }

    private suspend fun simulateProgressIncrease() {
        val targetProgress = 0.85f
        while (_progress.floatValue < targetProgress) {
            val increment = 0.01f + (kotlin.random.Random.nextFloat() * 0.02f)

            val newProgress = _progress.floatValue + increment
            _progress.floatValue = if (newProgress < targetProgress) newProgress else targetProgress

            delay(100)
        }
    }

    private suspend fun preloadData() = withContext(Dispatchers.IO) {
        try {
            highlightsRepository.getPlayers()

            delay(500)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}