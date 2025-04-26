package com.example.appgeneratorcursoria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.appgeneratorcursoria.presentation.composable.BottomNavigationBar
import com.example.appgeneratorcursoria.presentation.navigation.MainNavigation
import com.example.appgeneratorcursoria.presentation.navigation.Screen
import com.example.appgeneratorcursoria.ui.theme.AppGeneratorCursorIATheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            var showInfo by remember { mutableStateOf(false) }

            AppGeneratorCursorIATheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    
                    if (showInfo) {
                        AlertDialog(
                            onDismissRequest = { showInfo = false },
                            title = { Text("About Chat AI") },
                            text = { Text("This is a chat application powered by AI that helps you with conversations and tasks.") },
                            confirmButton = {
                                TextButton(onClick = { showInfo = false }) {
                                    Text("OK")
                                }
                            }
                        )
                    }

                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = { 
                                    Text(
                                        when (currentRoute) {
                                            Screen.Home.route -> "Home"
                                            Screen.Chat.route -> "Chat AI"
                                            Screen.Profile.route -> "Profile"
                                            Screen.Settings.route -> "Settings"
                                            else -> "Chat AI"
                                        },
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                actions = {
                                    // Botón de tema oscuro/claro
                                    IconButton(onClick = { isDarkTheme = !isDarkTheme }) {
                                        Icon(
                                            imageVector = Icons.Default.Build,
                                            contentDescription = "Toggle theme"
                                        )
                                    }
                                    // Botón de información
                                    IconButton(onClick = { showInfo = true }) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = "Information"
                                        )
                                    }
                                    if (currentRoute == Screen.Chat.route) {
                                        // Botón para limpiar conversación (solo en la pantalla de chat)
                                        IconButton(onClick = { /* TODO: Implementar limpieza de chat */ }) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "Clear chat"
                                            )
                                        }
                                    }
                                }
                            )
                        },
                        bottomBar = {
                            BottomNavigationBar(navController = navController)
                        }
                    ) { paddingValues ->
                        MainNavigation(
                            navController = navController,
                            modifier = Modifier.fillMaxSize(),
                            paddingValues = paddingValues
                        )
                    }
                }
            }
        }
    }
}