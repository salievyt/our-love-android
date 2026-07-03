package dev.deocore.outlove.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Task
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.deocore.outlove.ui.screens.AlbumScreen
import dev.deocore.outlove.ui.screens.DiaryScreen
import dev.deocore.outlove.ui.screens.HomeScreen
import dev.deocore.outlove.ui.screens.MoreScreen
import dev.deocore.outlove.ui.screens.TasksScreen

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    data object Home : BottomNavItem("home", "Главная", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
    data object Album : BottomNavItem("album", "Альбом", Icons.Filled.PhotoLibrary, Icons.Outlined.PhotoLibrary)
    data object Tasks : BottomNavItem("tasks", "Задания", Icons.Filled.Task, Icons.Outlined.Task)
    data object Diary : BottomNavItem("diary", "Дневник", Icons.Filled.Book, Icons.Outlined.Book)
    data object More : BottomNavItem("more", "Ещё", Icons.Filled.MoreHoriz, Icons.Outlined.MoreHoriz)
}

@Composable
fun MainNavigation() {
    var selectedTab by remember { mutableIntStateOf(0) }

    val navItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Album,
        BottomNavItem.Tasks,
        BottomNavItem.Diary,
        BottomNavItem.More,
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == index) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title, style = MaterialTheme.typography.labelSmall) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> HomeScreen(modifier = Modifier.padding(innerPadding))
            1 -> AlbumScreen(modifier = Modifier.padding(innerPadding))
            2 -> TasksScreen(modifier = Modifier.padding(innerPadding))
            3 -> DiaryScreen(modifier = Modifier.padding(innerPadding))
            4 -> MoreScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}
