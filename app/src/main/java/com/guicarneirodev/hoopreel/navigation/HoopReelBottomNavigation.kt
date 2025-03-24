package com.guicarneirodev.hoopreel.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.guicarneirodev.hoopreel.ui.theme.BasketballOrange
import com.guicarneirodev.hoopreel.ui.theme.NetflixDarkGray

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    data object Home : BottomNavItem(
        route = NavDestination.Highlights.route,
        icon = Icons.Default.Home,
        label = "Destaques"
    )

    data object Favorites : BottomNavItem(
        route = NavDestination.Favorites.route,
        icon = Icons.Default.Favorite,
        label = "Favoritos"
    )

    data object Search : BottomNavItem(
        route = NavDestination.Search.route,
        icon = Icons.Default.Search,
        label = "Buscar"
    )
}

@Composable
fun HoopReelBottomNavigation(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Search
    )

    NavigationBar(
        containerColor = NetflixDarkGray,
        contentColor = Color.White
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (isSelected) BasketballOrange else Color.White
                    )
                },
                selected = isSelected,
                onClick = {
                    onNavigate(item.route)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BasketballOrange,
                    selectedTextColor = BasketballOrange,
                    indicatorColor = NetflixDarkGray,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White
                )
            )
        }
    }
}