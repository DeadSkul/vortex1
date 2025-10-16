package com.geopulse.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.geopulse.app.data.AppState
import com.geopulse.app.ui.screens.*
import com.geopulse.app.ui.theme.GeoPulseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeoPulseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GeoPulseApp()
                }
            }
        }
    }
}

@Composable
fun GeoPulseApp() {
    val navController = rememberNavController()
    val appState: AppState = viewModel()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            WelcomeScreen(
                onGetStarted = {
                    navController.navigate("location")
                }
            )
        }

        composable("location") {
            LocationDateScreen(
                appState = appState,
                onNavigateToComfort = {
                    navController.navigate("comfort")
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("comfort") {
            ComfortProfileScreen(
                appState = appState,
                onNavigateToResults = {
                    navController.navigate("results")
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("results") {
            ResultsScreen(
                appState = appState,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToLocation = {
                    navController.navigate("location")
                }
            )
        }
    }
}
