package com.guicarneirodev.hoopreel.feature.favorites.di

import com.guicarneirodev.hoopreel.feature.favorites.data.FavoriteRepositoryImpl
import com.guicarneirodev.hoopreel.feature.favorites.domain.FavoriteRepository
import com.guicarneirodev.hoopreel.feature.favorites.domain.usecase.CheckFavoriteStatusUseCase
import com.guicarneirodev.hoopreel.feature.favorites.domain.usecase.GetFavoritesUseCase
import com.guicarneirodev.hoopreel.feature.favorites.domain.usecase.ToggleFavoriteUseCase
import com.guicarneirodev.hoopreel.feature.favorites.presentation.FavoriteViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val favoritesModule = module {
    factory<FavoriteRepository> { FavoriteRepositoryImpl(get()) }

    factory { CheckFavoriteStatusUseCase(get()) }
    factory { GetFavoritesUseCase(get()) }
    factory { ToggleFavoriteUseCase(get()) }

    viewModel { FavoriteViewModel(get()) }
}