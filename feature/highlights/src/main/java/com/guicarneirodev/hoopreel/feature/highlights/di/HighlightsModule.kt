package com.guicarneirodev.hoopreel.feature.highlights.di

import com.guicarneirodev.hoopreel.feature.highlights.data.HighlightsRepositoryImpl
import com.guicarneirodev.hoopreel.feature.highlights.domain.repository.HighlightsRepository
import com.guicarneirodev.hoopreel.feature.highlights.presentation.HighlightsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val highlightsModule = module {
    factory { HighlightsRepositoryImpl(get(),get(),get()) }
    factory<HighlightsRepository> { get<HighlightsRepositoryImpl>() }
    viewModel { HighlightsViewModel(get()) }
}