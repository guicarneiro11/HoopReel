package com.guicarneirodev.hoopreel.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.guicarneirodev.hoopreel.feature.highlights.ui.HighlightsScreen
import com.guicarneirodev.hoopreel.feature.player.ui.PlayerScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavDestination.Highlights.route
    ) {
        composable(NavDestination.Highlights.route) {
            HighlightsScreen(
                onVideoClick = { videoId ->
                    navController.navigate(
                        NavDestination.Player.createRoute(videoId)
                    )
                }
            )
        }

        composable(
            route = NavDestination.Player.route,
            arguments = listOf(
                navArgument("videoId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId")
                ?: return@composable

            PlayerScreen(
                videoId = videoId,
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    }
}