package com.guicarneirodev.hoopreel.core.di

import androidx.room.Room
import com.guicarneirodev.hoopreel.core.database.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().highlightDao() }
    single { get<AppDatabase>().lastUpdateDao() }
    single { get<AppDatabase>().favoriteDao() }
}