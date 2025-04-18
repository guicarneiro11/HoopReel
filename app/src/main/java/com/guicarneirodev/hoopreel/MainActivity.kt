package com.guicarneirodev.hoopreel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.guicarneirodev.hoopreel.navigation.AppNavigation
import com.guicarneirodev.hoopreel.navigation.HoopReelBottomNavigation
import com.guicarneirodev.hoopreel.navigation.NavDestination
import com.guicarneirodev.hoopreel.feature.settings.presentation.ThemeViewModel
import com.guicarneirodev.hoopreel.feature.splash.presentation.SplashViewModel
import com.guicarneirodev.hoopreel.feature.splash.ui.SplashScreen
import com.guicarneirodev.hoopreel.theme.HoopReelTheme
import com.guicarneirodev.hoopreel.theme.TeamBackgroundDecoration
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = koinViewModel()
            val currentTheme by themeViewModel.currentTheme.collectAsStateWithLifecycle()
            var showSplash by remember { mutableStateOf(true) }

            HoopReelTheme(currentTheme = currentTheme) {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = isSystemInDarkTheme()
                val statusBarColor = MaterialTheme.colorScheme.background

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = statusBarColor,
                        darkIcons = !useDarkIcons
                    )
                }

                if (showSplash) {
                    val splashViewModel: SplashViewModel = koinViewModel()
                    SplashScreen(
                        onLoadingComplete = { showSplash = false },
                        viewModel = splashViewModel
                    )
                } else {
                    TeamBackgroundDecoration {
                        MainContent()
                    }
                }
            }
        }
    }
}

@Composable
fun MainContent() {
    val navController = rememberNavController()
    var isNavGraphReady by remember { mutableStateOf(false) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = when {
        !isNavGraphReady -> false
        currentRoute?.startsWith("player/") == true -> false
        currentRoute == NavDestination.ThemeSelection.route -> false
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
        TeamBackgroundDecoration {
            Box(modifier = Modifier.padding(paddingValues)) {
                AppNavigation(navController = navController)
                LaunchedEffect(navController) {
                    isNavGraphReady = true
                }
            }
        }
    }
}