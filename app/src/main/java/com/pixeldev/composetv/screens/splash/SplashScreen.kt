package com.pixeldev.composetv.screens.splash
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.pixeldev.composetv.graph.Screen

import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
) {

    // Navigate after checking saved login
    LaunchedEffect(Unit) {
        delay(1500) // Optional splash delay for smoother transition
        navController.navigate(Screen.DashBoardScreen.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    // Splash UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Checking session...",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}