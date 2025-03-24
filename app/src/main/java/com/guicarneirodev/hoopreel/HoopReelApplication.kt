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

class HoopReelApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@HoopReelApplication)
            modules(
                coreModule,
                networkModule,
                databaseModule,
                playerModule,
                highlightsModule,
                favoritesModule,
                searchModule
            )
        }
    }
}