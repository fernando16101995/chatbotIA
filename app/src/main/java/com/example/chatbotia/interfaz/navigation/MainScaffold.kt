package com.example.chatbotia.interfaz.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chatbotia.interfaz.chat.ChatScreen
import com.example.chatbotia.interfaz.history.HistoryScreen
import com.example.chatbotia.interfaz.profile.ProfileScreen
import com.example.chatbotia.interfaz.theme.*

sealed class MainTab(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Chat : MainTab("chat", "Chat", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline)
    object History : MainTab("history", "Historial", Icons.Filled.History, Icons.Outlined.History)
    object Profile : MainTab("profile", "Perfil", Icons.Filled.Person, Icons.Outlined.Person)
}

val mainTabs = listOf(MainTab.Chat, MainTab.History, MainTab.Profile)

@Composable
fun MainScaffold(onLogout: () -> Unit) {
    val navController = rememberNavController()

    Scaffold(
        containerColor = BgDark,
        bottomBar = {
            SerenBottomBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MainTab.Chat.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MainTab.Chat.route) {
                ChatScreen(onNavigateToProfile = {
                    navController.navigate(MainTab.Profile.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
            composable(MainTab.History.route) {
                HistoryScreen()
            }
            composable(MainTab.Profile.route) {
                ProfileScreen(
                    onBack = {
                        navController.navigate(MainTab.Chat.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onLogout = onLogout
                )
            }
        }
    }
}

@Composable
fun SerenBottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar(
        containerColor = SurfaceDark,
        tonalElevation = 0.dp,
        modifier = Modifier.height(64.dp)
    ) {
        mainTabs.forEach { tab ->
            val selected = currentRoute == tab.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Box(contentAlignment = Alignment.Center) {
                        if (selected) {
                            Box(
                                modifier = Modifier
                                    .width(56.dp)
                                    .height(28.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(AccentViolet.copy(alpha = 0.15f))
                            )
                        }
                        Icon(
                            imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                            contentDescription = tab.label,
                            modifier = Modifier.size(20.dp),
                            tint = if (selected) AccentViolet else TextSecondary
                        )
                    }
                },
                label = {
                    Text(
                        tab.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selected) AccentViolet else TextSecondary
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                    selectedIconColor = AccentViolet,
                    unselectedIconColor = TextSecondary
                )
            )
        }
    }
}
