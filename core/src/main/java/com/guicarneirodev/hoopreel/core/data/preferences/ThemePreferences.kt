package com.guicarneirodev.hoopreel.core.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.guicarneirodev.hoopreel.core.theme.NbaTeam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

class ThemePreferences(private val context: Context) {
    private val dataStore = context.themeDataStore

    val teamTheme: Flow<NbaTeam> = dataStore.data.map { preferences ->
        val teamId = preferences[SELECTED_TEAM_ID] ?: NbaTeam.DEFAULT.teamId
        NbaTeam.getById(teamId)
    }

    suspend fun setTeamTheme(team: NbaTeam) {
        dataStore.edit { preferences ->
            preferences[SELECTED_TEAM_ID] = team.teamId
        }
    }

    companion object {
        private val SELECTED_TEAM_ID = stringPreferencesKey("selected_team_id")
    }
}