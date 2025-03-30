package com.guicarneirodev.hoopreel.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.guicarneirodev.hoopreel.core.theme.NbaTeam
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            if (teamTheme != NbaTeam.DEFAULT && teamTheme.logoUrl.isNotEmpty()) {
                TeamLogoPattern(
                    teamLogoUrl = teamTheme.logoUrl,
                    teamTheme = teamTheme
                )
            }
        }

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

    // Número otimizado de ícones para manter a fluidez
    val logoCount = 150 // Reduzido para um número que mantém fluidez
    val random = Random(teamTheme.teamId.hashCode())

    // Tamanhos para os ícones
    val smallSize = 28.dp
    val mediumSize = 38.dp
    val largeSize = 48.dp

    // Opacidade para melhor visibilidade
    val minOpacity = 0.15f
    val maxOpacity = 0.28f

    Box(modifier = Modifier.fillMaxSize()) {
        // Dividimos em 3 grupos com diferentes velocidades para otimizar
        val fastIcons = (0 until logoCount/3)
        val mediumIcons = (logoCount/3 until 2*logoCount/3)
        val slowIcons = (2*logoCount/3 until logoCount)

        // 1. GRUPO RÁPIDO - Movimento mais simples e direto
        fastIcons.forEach { index ->
            val iconSeed = random.nextLong() + index
            val iconRandom = Random(iconSeed)

            val size = when {
                iconRandom.nextFloat() < 0.6f -> smallSize
                iconRandom.nextFloat() < 0.9f -> mediumSize
                else -> largeSize
            }

            val opacity = minOpacity + (iconRandom.nextFloat() * (maxOpacity - minOpacity))
            val initialRotation = iconRandom.nextFloat() * 360f

            // Posição inicial espalhada pela tela
            val initialX = iconRandom.nextFloat() * configuration.screenWidthDp
            val initialY = iconRandom.nextFloat() * configuration.screenHeightDp

            // Movimento diagonal simples
            val angle = iconRandom.nextFloat() * 360f
            val speed = 12000 + (iconRandom.nextInt(8000))  // 12-20 segundos

            val infiniteTransition = rememberInfiniteTransition(label = "fast$index")

            // Animação de posição usando apenas uma transformação com ângulo
            val progress by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(speed, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "progress$index"
            )

            // Calcular posição com base no ângulo e progresso
            val distance = configuration.screenWidthDp * 1.5f

            // Posição atual - movimento diagonal
            val xOffset = cos(angle * PI.toFloat() / 180) * distance * progress
            val yOffset = sin(angle * PI.toFloat() / 180) * distance * progress

            // Posição na tela com wrap-around
            val xPosition = (initialX + xOffset).mod(configuration.screenWidthDp.toDouble())
            val yPosition = (initialY + yOffset).mod(configuration.screenHeightDp.toDouble())

            // Desenhar com key para otimizar recomposições
            key(index) {
                AsyncImage(
                    model = teamLogoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(size)
                        .offset(x = xPosition.dp, y = yPosition.dp)
                        .graphicsLayer(
                            alpha = opacity,
                            rotationZ = initialRotation
                        ),
                    contentScale = ContentScale.Fit
                )
            }
        }

        // 2. GRUPO MÉDIO - Movimento mais lento e suave
        mediumIcons.forEach { index ->
            val iconSeed = random.nextLong() + index
            val iconRandom = Random(iconSeed)

            val size = when {
                iconRandom.nextFloat() < 0.6f -> smallSize
                iconRandom.nextFloat() < 0.9f -> mediumSize
                else -> largeSize
            }

            val opacity = minOpacity + (iconRandom.nextFloat() * (maxOpacity - minOpacity))
            val initialRotation = iconRandom.nextFloat() * 360f

            // Posição inicial
            val initialX = iconRandom.nextFloat() * configuration.screenWidthDp
            val initialY = iconRandom.nextFloat() * configuration.screenHeightDp

            // Movimento mais lento
            val angle = iconRandom.nextFloat() * 360f
            val speed = 18000 + (iconRandom.nextInt(12000))  // 18-30 segundos

            // Usar um único valor animado para melhorar performance
            val progress by rememberInfiniteTransition(label = "medium$index").animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(speed, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "mediumProgress$index"
            )

            // Movimento curvo
            val distance = configuration.screenWidthDp * 1.2f
            val curveIntensity = 0.3f

            val xOffset = cos(angle * PI.toFloat() / 180) * distance * progress
            val yOffset = sin(angle * PI.toFloat() / 180) * distance * progress
            val curveFactor = sin(progress * 2 * PI.toFloat()) * configuration.screenWidthDp * curveIntensity

            val xPosition = (initialX + xOffset + curveFactor).mod(
                configuration.screenWidthDp.toDouble()
            )
            val yPosition = (initialY + yOffset).mod(configuration.screenHeightDp.toDouble())

            key(index + logoCount) {
                AsyncImage(
                    model = teamLogoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(size)
                        .offset(x = xPosition.dp, y = yPosition.dp)
                        .graphicsLayer(
                            alpha = opacity,
                            rotationZ = initialRotation + (progress * 360 * if (iconRandom.nextBoolean()) 1f else -1f)
                        ),
                    contentScale = ContentScale.Fit
                )
            }
        }

        // 3. GRUPO LENTO - Movimento flutuante e suave
        slowIcons.forEach { index ->
            val iconSeed = random.nextLong() + index + 1000 // Seed diferente
            val iconRandom = Random(iconSeed)

            val size = when {
                iconRandom.nextFloat() < 0.5f -> smallSize
                iconRandom.nextFloat() < 0.8f -> mediumSize
                else -> largeSize
            }

            val opacity = minOpacity + (iconRandom.nextFloat() * (maxOpacity - minOpacity))

            // Posição inicial
            val initialX = iconRandom.nextFloat() * configuration.screenWidthDp
            val initialY = iconRandom.nextFloat() * configuration.screenHeightDp

            // Flutuação suave (pequenos movimentos)
            val transition = rememberInfiniteTransition(label = "slow$index")

            val xFloat by transition.animateFloat(
                initialValue = -8f,
                targetValue = 8f,
                animationSpec = infiniteRepeatable(
                    animation = tween(8000, easing = EaseInOutSine),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "xSlow$index"
            )

            val yFloat by transition.animateFloat(
                initialValue = -6f,
                targetValue = 6f,
                animationSpec = infiniteRepeatable(
                    animation = tween(7000, easing = EaseInOutSine),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "ySlow$index"
            )

            val rotation by transition.animateFloat(
                initialValue = -5f,
                targetValue = 5f,
                animationSpec = infiniteRepeatable(
                    animation = tween(9000, easing = EaseInOutSine),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "rotateSlow$index"
            )

            key(index + 2*logoCount) {
                AsyncImage(
                    model = teamLogoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(size)
                        .offset(
                            x = (initialX + xFloat).dp,
                            y = (initialY + yFloat).dp
                        )
                        .graphicsLayer(
                            alpha = opacity,
                            rotationZ = iconRandom.nextFloat() * 360 + rotation
                        ),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

private val EaseInOutSine = CubicBezierEasing(0.37f, 0.0f, 0.63f, 1.0f)