package com.guicarneirodev.hoopreel.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guicarneirodev.hoopreel.core.data.preferences.ThemePreferences
import com.guicarneirodev.hoopreel.core.theme.NbaTeam
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val themePreferences: ThemePreferences
) : ViewModel() {

    val currentTheme: StateFlow<NbaTeam> = themePreferences.teamTheme
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            NbaTeam.DEFAULT
        )

    val allTeams = NbaTeam.entries

    fun setTeamTheme(team: NbaTeam) {
        viewModelScope.launch {
            themePreferences.setTeamTheme(team)
        }
    }
}