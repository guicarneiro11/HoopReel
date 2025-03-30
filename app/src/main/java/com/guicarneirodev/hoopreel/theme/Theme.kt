package com.guicarneirodev.hoopreel.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.guicarneirodev.hoopreel.core.theme.NbaTeam

val LocalTeamTheme = compositionLocalOf { NbaTeam.DEFAULT }

@Composable
fun HoopReelTheme(
    currentTheme: NbaTeam,
    content: @Composable () -> Unit
) {
    val colorScheme = if (currentTheme.isDarkTheme) {
        darkColorScheme(
            primary = currentTheme.primaryColor,
            secondary = currentTheme.secondaryColor,
            background = Color(0xFF000000),
            surface = Color(0xFF141414),
            onBackground = Color.White,
            onSurface = Color.White
        )
    } else {
        lightColorScheme(
            primary = currentTheme.primaryColor,
            secondary = currentTheme.secondaryColor,
            background = Color.White,
            surface = Color(0xFFF5F5F5),
            onBackground = Color.Black,
            onSurface = Color.Black
        )
    }

    CompositionLocalProvider(
        LocalTeamTheme provides currentTheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = {
                if (currentTheme != NbaTeam.DEFAULT && currentTheme.logoUrl.isNotEmpty()) {
                    TeamBackgroundDecoration {
                        content()
                    }
                } else {
                    content()
                }
            }
        )
    }
}

@Composable
fun TeamBackgroundDecoration(
    content: @Composable BoxScope.() -> Unit
) {
    val teamTheme = LocalTeamTheme.current
    val density = LocalDensity.current
    LocalConfiguration.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Gradiente de fundo sutil
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            teamTheme.primaryColor.copy(alpha = 0.03f),
                            teamTheme.primaryColor.copy(alpha = 0.08f)
                        )
                    )
                )
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val positions = listOf(
                    Offset(size.width * 0.1f, size.height * 0.15f),
                    Offset(size.width * 0.7f, size.height * 0.25f),
                    Offset(size.width * 0.2f, size.height * 0.5f),
                    Offset(size.width * 0.85f, size.height * 0.6f),
                    Offset(size.width * 0.5f, size.height * 0.8f),
                    Offset(size.width * 0.15f, size.height * 0.9f)
                )

                val rotations = listOf(0f, 15f, -10f, 5f, -5f, 20f)
                val scales = listOf(1.0f, 0.8f, 1.2f, 0.9f, 1.1f, 0.85f)

                // Criar círculos decorativos com as cores do time
                positions.forEachIndexed { index, offset ->
                    val logoSize = with(density) { 60.dp.toPx() * scales[index % scales.size] }

                    withTransform({
                        translate(offset.x, offset.y)
                        rotate(rotations[index % rotations.size])
                    }) {
                        // Círculo principal
                        drawCircle(
                            color = teamTheme.primaryColor.copy(alpha = 0.07f),
                            radius = logoSize,
                            center = Offset(0f, 0f)
                        )

                        // Círculo de borda/acento
                        drawCircle(
                            color = teamTheme.secondaryColor.copy(alpha = 0.05f),
                            radius = logoSize * 0.85f,
                            center = Offset(0f, 0f),
                            style = Stroke(width = logoSize * 0.1f)
                        )
                    }
                }
            }
        }

        // Conteúdo principal
        content()
    }
}