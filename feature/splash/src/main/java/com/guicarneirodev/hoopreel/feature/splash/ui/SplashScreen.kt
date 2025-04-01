package com.guicarneirodev.hoopreel.feature.splash.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guicarneirodev.hoopreel.feature.splash.presentation.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onLoadingComplete: () -> Unit,
    viewModel: SplashViewModel
) {
    // Coletamos os valores de StateFlow usando collectAsStateWithLifecycle
    val progress by viewModel.progress.collectAsStateWithLifecycle()
    val isLoadingComplete by viewModel.isLoadingComplete.collectAsStateWithLifecycle()

    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "AlphaAnimation"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        viewModel.startLoading()
    }

    LaunchedEffect(key1 = isLoadingComplete) {
        if (isLoadingComplete) {
            delay(500)
            onLoadingComplete()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .alpha(alphaAnim),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Basketball Icon
            Icon(
                imageVector = Icons.Default.SportsBasketball,
                contentDescription = "Basketball",
                tint = Color(0xFFFF6B00),
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // App Name
            Text(
                text = "HoopReel",
                color = Color(0xFFFF6B00),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "NBA Highlights Experience",
                color = Color.White,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Progress Bar - agora passamos o Float diretamente
            LinearProgressIndicator(
                progress = {
                    progress  // Não é mais uma lambda
                },
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFFF6B00),
                trackColor = Color(0xFF1E1E1E),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Text
            Text(
                text = "${(progress * 100).toInt()}%",
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}