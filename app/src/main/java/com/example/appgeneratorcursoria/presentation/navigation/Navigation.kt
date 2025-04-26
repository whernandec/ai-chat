package com.example.appgeneratorcursoria.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Face
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    data object Home : Screen("home", Icons.Default.Home, "Home")
    data object Chat : Screen("chat", Icons.Default.Face, "Chat")
    data object Profile : Screen("profile", Icons.Default.Person, "Profile")
    data object Settings : Screen("settings", Icons.Default.Settings, "Settings")
}
