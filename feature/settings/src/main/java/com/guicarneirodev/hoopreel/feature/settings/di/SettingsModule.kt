package com.guicarneirodev.hoopreel.feature.settings.di

import com.guicarneirodev.hoopreel.core.data.preferences.ThemePreferences
import com.guicarneirodev.hoopreel.feature.settings.presentation.ThemeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single { ThemePreferences(get()) }
    viewModel { ThemeViewModel(get()) }
}