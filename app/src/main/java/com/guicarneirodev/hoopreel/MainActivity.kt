package com.guicarneirodev.hoopreel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.guicarneirodev.hoopreel.navigation.AppNavigation
import com.guicarneirodev.hoopreel.navigation.HoopReelBottomNavigation
import com.guicarneirodev.hoopreel.navigation.NavDestination
import com.guicarneirodev.hoopreel.ui.theme.HoopReelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HoopReelTheme {
                val navController = rememberNavController()

                var isNavGraphReady by remember { mutableStateOf(false) }

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val showBottomBar = when {
                    !isNavGraphReady -> false
                    currentRoute?.startsWith("player/") == true -> false
                    else -> true
                }

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            HoopReelBottomNavigation(
                                currentRoute = currentRoute,
                                onNavigate = { route ->
                                    if (isNavGraphReady && currentRoute != route) {
                                        navController.navigate(route) {
                                            popUpTo(NavDestination.Highlights.route) {
                                                saveState = true
                                                inclusive = false
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        AppNavigation(navController = navController)

                        LaunchedEffect(navController) {
                            isNavGraphReady = true
                        }
                    }
                }
            }
        }
    }
}