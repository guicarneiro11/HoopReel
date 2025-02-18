package com.guicarneirodev.hoopreel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.guicarneirodev.hoopreel.feature.player.ui.PlayerScreen
import com.guicarneirodev.hoopreel.ui.theme.HoopReelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HoopReelTheme {
                PlayerScreen(videoId = "jdxNWkQyV-w")
            }
        }
    }
}