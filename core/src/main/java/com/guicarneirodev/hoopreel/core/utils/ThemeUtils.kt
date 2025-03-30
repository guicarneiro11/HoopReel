package com.guicarneirodev.hoopreel.core.utils

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.guicarneirodev.hoopreel.core.theme.NbaTeam

val LocalTeamTheme = compositionLocalOf { NbaTeam.DEFAULT }

object ThemeColors {
    val BasketballOrange = Color(0xFFFF6B00)
    val BasketballLightOrange = Color(0xFFFF8534)
    val NetflixBlack = Color(0xFF000000)
    val NetflixDarkGray = Color(0xFF141414)
    val NetflixLightGray = Color(0xFF2F2F2F)
}