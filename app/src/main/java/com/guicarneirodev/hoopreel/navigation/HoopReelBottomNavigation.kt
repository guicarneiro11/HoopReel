package com.guicarneirodev.hoopreel.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.guicarneirodev.hoopreel.theme.LocalTeamTheme

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

    data object Statistics : BottomNavItem(
        route = NavDestination.Statistics.route,
        icon = Icons.Default.BarChart,
        label = "Estatísticas"
    )

    data object ThemesNav : BottomNavItem(
        route = NavDestination.ThemeSelection.route,
        icon = Icons.Default.Palette,
        label = "Temas"
    )
}

@Composable
fun HoopReelBottomNavigation(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val teamTheme = LocalTeamTheme.current
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Search,
        BottomNavItem.Statistics,
        BottomNavItem.ThemesNav
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
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
                        color = if (isSelected) teamTheme.primaryColor else MaterialTheme.colorScheme.onSurface,
                        fontSize = 10.sp,
                        maxLines = 1,
                        minLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                selected = isSelected,
                onClick = {
                    onNavigate(item.route)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = teamTheme.primaryColor,
                    selectedTextColor = teamTheme.primaryColor,
                    indicatorColor = MaterialTheme.colorScheme.surface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}