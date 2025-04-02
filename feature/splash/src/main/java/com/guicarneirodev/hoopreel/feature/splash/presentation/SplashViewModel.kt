package com.guicarneirodev.hoopreel.feature.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guicarneirodev.hoopreel.feature.highlights.domain.repository.HighlightsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel(
    private val highlightsRepository: HighlightsRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _isLoadingComplete = MutableStateFlow(false)
    val isLoadingComplete: StateFlow<Boolean> = _isLoadingComplete.asStateFlow()

    fun startLoading() {
        viewModelScope.launch {
            val progressJob = launch {
                simulateProgressIncrease()
            }

            val dataLoadJob = launch {
                preloadData()
            }

            dataLoadJob.join()

            while (_progress.value < 1f) {
                _progress.value = minOf(_progress.value + 0.05f, 1f)
                delay(50)
            }

            progressJob.cancel()

            _isLoadingComplete.value = true
        }
    }

    private suspend fun simulateProgressIncrease() {
        // Incrementa o progresso gradualmente até 85% para dar margem ao carregamento real
        val targetProgress = 0.85f
        while (_progress.value < targetProgress) {
            // Incremento com aleatoriedade para parecer mais natural
            val increment = 0.01f + (kotlin.random.Random.nextFloat() * 0.02f)

            val newProgress = _progress.value + increment
            _progress.value = if (newProgress < targetProgress) newProgress else targetProgress

            delay(100)
        }
    }

    private suspend fun preloadData() = withContext(ioDispatcher) {  // Usando o dispatcher injetado
        try {
            // Carregar dados essenciais para a tela inicial
            highlightsRepository.getPlayers()

            // Simulando algum processamento adicional
            delay(500)

        } catch (e: Exception) {
            // Em caso de falha, ainda permite prosseguir
            e.printStackTrace()
        }
    }
}