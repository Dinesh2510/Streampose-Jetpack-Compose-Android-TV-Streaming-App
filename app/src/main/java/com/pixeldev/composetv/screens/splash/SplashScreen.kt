package com.pixeldev.composetv.screens.splash
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.pixeldev.composetv.R
import com.pixeldev.composetv.graph.Screen
import com.pixeldev.composetv.utlis.Constants.Companion.StreamposeBackground

import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    navController: NavController // if you want to navigate after splash
) {
    // Animation state
    var scale by remember { mutableFloatStateOf(0f) }

    // Navigate after checking saved login
    LaunchedEffect(Unit) {
        delay(1500) // Optional splash delay for smoother transition
        navController.navigate(Screen.DashBoardScreen.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    // UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = StreamposeBackground), // 🔥 gradient bg
        contentAlignment = Alignment.Center
    ) {
        // Logo with scale animation
        Image(
            painter = painterResource(id = R.drawable.logo_trans),
            contentDescription = "Streampose Logo",
            modifier = Modifier
                .size(400.dp)
        )
    }
}
