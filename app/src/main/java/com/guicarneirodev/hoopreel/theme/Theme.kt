package com.guicarneirodev.hoopreel.theme

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withSave
import coil.compose.AsyncImage
import com.guicarneirodev.hoopreel.core.theme.NbaTeam
import kotlin.random.Random

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
            surfaceVariant = Color(0xFF1E1E1E),
            onBackground = Color.White,
            onSurface = Color.White,
            onSurfaceVariant = Color.White
        )
    } else {
        lightColorScheme(
            primary = currentTheme.primaryColor,
            secondary = currentTheme.secondaryColor,
            background = Color.White,
            surface = Color(0xFFF5F5F5),
            surfaceVariant = Color(0xFFE0E0E0),
            onBackground = Color.Black,
            onSurface = Color.Black,
            onSurfaceVariant = Color.Black
        )
    }

    CompositionLocalProvider(
        LocalTeamTheme provides currentTheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = {
                TeamBackgroundDecoration {
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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fundo preto sólido
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Se o time tiver um logo, usamos para criar o padrão
            if (teamTheme != NbaTeam.DEFAULT && teamTheme.logoUrl.isNotEmpty()) {
                TeamLogoPattern(
                    teamLogoUrl = teamTheme.logoUrl,
                    teamTheme = teamTheme
                )
            }
        }

        // Conteúdo principal sobreposto à decoração
        content()
    }
}

@Composable
private fun TeamLogoPattern(
    teamLogoUrl: String,
    teamTheme: NbaTeam
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    // Animações de flutuação
    val infiniteTransition = rememberInfiniteTransition(label = "backgroundAnimation")

    // Vários deslocamentos com diferentes velocidades e amplitudes
    val verticalOffset1 by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "verticalOffset1"
    )

    val verticalOffset2 by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "verticalOffset2"
    )

    val horizontalOffset1 by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "horizontalOffset1"
    )

    // Criamos um grid mais denso de ícones
    val logoCount = 350 // Aumentado de 200 para 350
    val random = Random(teamTheme.teamId.hashCode())

    // Tamanhos maiores para os ícones
    val smallSize = 24.dp  // Aumentado
    val mediumSize = 34.dp // Aumentado
    val largeSize = 46.dp  // Aumentado

    // Maior opacidade para os ícones
    val minOpacity = 0.15f // Aumentado de 0.06f para 0.15f
    val maxOpacity = 0.25f // Aumentado de 0.14f para 0.25f

    // Geramos posições aleatórias para os ícones cobrindo toda a tela
    Box(modifier = Modifier.fillMaxSize()) {
        // Dividimos o total em grupos para carregamento mais eficiente
        (0 until logoCount).chunked(50).forEach { chunk ->
            chunk.forEach { index ->
                // Usamos um seed diferente para cada ícone, mas consistente entre renderizações
                val iconSeed = random.nextLong() + index
                val iconRandom = Random(iconSeed)

                // Posição aleatória, mas garantindo distribuição melhor
                val xSection = index % 7 // Dividimos em 7 colunas
                val ySection = index / 7 % 10 // E 10 linhas

                // Variação dentro de cada seção
                val xOffset = iconRandom.nextFloat() * 0.14f
                val yOffset = iconRandom.nextFloat() * 0.1f

                // Posição base da seção + variação
                val xPos = (xSection * 0.14f + xOffset) * configuration.screenWidthDp
                val yPos = (ySection * 0.1f + yOffset) * configuration.screenHeightDp

                // Tamanho aleatório com distribuição balanceada
                val size = when {
                    iconRandom.nextFloat() < 0.5f -> smallSize // 50% pequenos
                    iconRandom.nextFloat() < 0.8f -> mediumSize // 30% médios
                    else -> largeSize // 20% grandes
                }

                // Opacidade aprimorada
                val opacity = minOpacity + (iconRandom.nextFloat() * (maxOpacity - minOpacity))

                // Rotação aleatória
                val rotation = iconRandom.nextFloat() * 360f

                // Decidir qual animação aplicar a este ícone
                val animationGroup = iconRandom.nextInt(4)
                val xAnimation = when (animationGroup) {
                    0 -> horizontalOffset1
                    else -> 0f
                }

                val yAnimation = when (animationGroup) {
                    1 -> verticalOffset1
                    2 -> verticalOffset2
                    else -> 0f
                }

                AsyncImage(
                    model = teamLogoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(size)
                        .offset(
                            x = (xPos + xAnimation).dp,
                            y = (yPos + yAnimation).dp
                        )
                        .graphicsLayer(
                            alpha = opacity,
                            rotationZ = rotation
                        ),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
private fun Float.toDp() = with(LocalDensity.current) { this@toDp.toDp() }

@Composable
private fun TeamLogoDecorations(
    teamLogoUrl: String,
    teamTheme: NbaTeam
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Posições e tamanhos diversos para os logos
    val decorations = listOf(
        // Posição (em porcentagem da tela), tamanho, rotação, alpha
        Triple(Offset(0.05f, 0.1f), 60.dp, 5f),
        Triple(Offset(0.75f, 0.15f), 80.dp, -15f),
        Triple(Offset(0.2f, 0.35f), 70.dp, 20f),
        Triple(Offset(0.85f, 0.5f), 90.dp, 0f),
        Triple(Offset(0.4f, 0.7f), 65.dp, -10f),
        Triple(Offset(0.1f, 0.75f), 75.dp, 30f),
        Triple(Offset(0.6f, 0.9f), 55.dp, -5f)
    )

    decorations.forEach { (offsetPercent, size, rotation) ->
        val xPos = with(density) { (screenWidth.toPx() * offsetPercent.x) }
        val yPos = with(density) { (screenHeight.toPx() * offsetPercent.y) }

        Box(
            modifier = Modifier
                .size(size)
                .offset(
                    x = with(density) { xPos.toDp() },
                    y = with(density) { yPos.toDp() }
                )
        ) {
            // Carrega o logo do time com opacidade reduzida
            AsyncImage(
                model = teamLogoUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        alpha = 0.12f, // Logo bem sutil
                        rotationZ = rotation
                    ),
                contentScale = ContentScale.Fit
            )
        }
    }

    // Efeito adicional: círculos com as cores do time em posições diferentes
    Canvas(modifier = Modifier.fillMaxSize()) {
        val circlesToDraw = 4
        val radius = size.minDimension * 0.18f

        for (i in 0 until circlesToDraw) {
            val xPos = size.width * (0.2f + (i * 0.25f))
            val yPos = size.height * (0.3f + (i * 0.15f))
            val circleSize = radius * (0.7f + (i * 0.15f))

            // Círculo primário
            drawCircle(
                color = teamTheme.primaryColor.copy(alpha = 0.08f),
                radius = circleSize,
                center = Offset(xPos, yPos)
            )

            // Círculo secundário (menor)
            drawCircle(
                color = teamTheme.secondaryColor.copy(alpha = 0.06f),
                radius = circleSize * 0.6f,
                center = Offset(xPos, yPos),
                style = Stroke(width = circleSize * 0.1f)
            )
        }
    }
}