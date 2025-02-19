package com.guicarneirodev.hoopreel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.guicarneirodev.hoopreel.navigation.AppNavigation
import com.guicarneirodev.hoopreel.ui.theme.HoopReelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HoopReelTheme {
                AppNavigation()
            }
        }
    }
}