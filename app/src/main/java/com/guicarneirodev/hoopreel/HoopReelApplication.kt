package com.guicarneirodev.hoopreel

import android.app.Application
import com.guicarneirodev.hoopreel.core.di.coreModule
import com.guicarneirodev.hoopreel.core.di.databaseModule
import com.guicarneirodev.hoopreel.core.di.networkModule
import com.guicarneirodev.hoopreel.feature.highlights.di.highlightsModule
import com.guicarneirodev.hoopreel.feature.player.di.playerModule
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
            )
        }
    }
}