package com.guicarneirodev.hoopreel.feature.highlights.di

import com.guicarneirodev.hoopreel.feature.highlights.presentation.StatisticsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val statisticsModule = module {
    viewModel { StatisticsViewModel(get(), get()) }
}