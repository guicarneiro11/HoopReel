package com.guicarneirodev.hoopreel.feature.player.di

import com.guicarneirodev.hoopreel.feature.player.data.repository.PlayerRepositoryImpl
import com.guicarneirodev.hoopreel.feature.player.domain.repository.PlayerRepository
import com.guicarneirodev.hoopreel.feature.player.presentation.PlayerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {
    factory { PlayerRepositoryImpl(get()) }
    factory<PlayerRepository> { get<PlayerRepositoryImpl>() }
    viewModel { PlayerViewModel(get()) }
}