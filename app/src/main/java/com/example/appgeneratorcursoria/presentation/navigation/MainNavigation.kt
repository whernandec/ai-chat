package com.example.appgeneratorcursoria.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.appgeneratorcursoria.presentation.home.HomeScreen
import com.example.appgeneratorcursoria.presentation.profile.ProfileScreen
import com.example.appgeneratorcursoria.presentation.settings.SettingsScreen
import com.example.appgeneratorcursoria.presentation.chat.ChatScreen
import com.example.appgeneratorcursoria.presentation.chat.ChatViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost

@Composable
fun MainNavigation(
    navController: NavController,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Chat.route) {
            val viewModel: ChatViewModel = hiltViewModel()
            val isLoading by viewModel.isLoading.collectAsState()
            
            ChatScreen(
                isLoading = isLoading,
                error = null,
                onClearError = {
                    viewModel.clearChat()
                },
                paddingValues = paddingValues
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}

