package com.guicarneirodev.hoopreel.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BasketballOrange,
    secondary = BasketballLightOrange,
    background = NetflixBlack,
    surface = NetflixDarkGray,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun HoopReelTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}