package com.guicarneirodev.hoopreel

import android.app.Application
import com.guicarneirodev.hoopreel.core.di.coreModule
import com.guicarneirodev.hoopreel.core.di.databaseModule
import com.guicarneirodev.hoopreel.core.di.networkModule
import com.guicarneirodev.hoopreel.feature.favorites.di.favoritesModule
import com.guicarneirodev.hoopreel.feature.highlights.di.highlightsModule
import com.guicarneirodev.hoopreel.feature.player.di.playerModule
import com.guicarneirodev.hoopreel.feature.search.di.searchModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import androidx.work.*
import com.guicarneirodev.hoopreel.feature.highlights.di.statisticsModule
import com.guicarneirodev.hoopreel.feature.highlights.di.workManagerModule
import com.guicarneirodev.hoopreel.feature.highlights.workers.HighlightsUpdateWorker
import com.guicarneirodev.hoopreel.feature.settings.di.settingsModule
import org.koin.androidx.workmanager.koin.workManagerFactory
import java.util.concurrent.TimeUnit

class HoopReelApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Koin setup
        startKoin {
            androidContext(this@HoopReelApplication)
            workManagerFactory()
            modules(
                coreModule,
                networkModule,
                databaseModule,
                playerModule,
                highlightsModule,
                favoritesModule,
                searchModule,
                workManagerModule,
                statisticsModule,
                settingsModule
            )
        }

        setupWorkManager()
    }

    private fun setupWorkManager() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val updateHighlightsRequest = PeriodicWorkRequestBuilder<HighlightsUpdateWorker>(
            1, TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                30, TimeUnit.MINUTES
            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "update_highlights",
            ExistingPeriodicWorkPolicy.UPDATE,
            updateHighlightsRequest
        )
    }
}