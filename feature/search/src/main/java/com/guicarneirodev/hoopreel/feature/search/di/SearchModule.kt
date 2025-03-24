package com.guicarneirodev.hoopreel.feature.search.di

import com.guicarneirodev.hoopreel.feature.search.data.SearchRepositoryImpl
import com.guicarneirodev.hoopreel.feature.search.domain.repository.SearchRepository
import com.guicarneirodev.hoopreel.feature.search.domain.usecase.GetRecentSearchesUseCase
import com.guicarneirodev.hoopreel.feature.search.domain.usecase.SearchVideosUseCase
import com.guicarneirodev.hoopreel.feature.search.presentation.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    factory<SearchRepository> {
        SearchRepositoryImpl(
            youTubeApiService = get(),
            highlightsRepository = get()
        )
    }

    factory { SearchVideosUseCase(get()) }
    factory { GetRecentSearchesUseCase(get()) }

    viewModel { SearchViewModel(get(), get()) }
}