package com.guicarneirodev.hoopreel.feature.highlights.di

import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import com.guicarneirodev.hoopreel.feature.highlights.workers.HighlightsUpdateWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.bind
import org.koin.dsl.module

val workManagerModule = module {
    single { WorkManager.getInstance(get()) }

    workerOf(::HighlightsUpdateWorker) {
        bind<CoroutineWorker>()
    }
}