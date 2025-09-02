package com.pixeldev.composetv.screens.home

import android.os.Process
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Text
import androidx.tv.material3.rememberDrawerState
import androidx.tv.material3.*

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), navController: NavHostController) {
    val showExitDialog = remember { mutableStateOf(false) }
    //Exit Dialog Code
    if (showExitDialog.value) {
        /*IncludeApp().showExitDialog {
            showExitDialog.value = false
        }*/
    }
    BackHandler(enabled = true) {
        if (showExitDialog.value) {
            // Exit the app
            Process.killProcess(Process.myPid())
        } else {
            showExitDialog.value = true
        }
    }


    val discoveryMovieState by viewModel.discoveryMovieResponses.collectAsState()
    val trendingMovieState by viewModel.trendingMovieResponses.collectAsState()
    val nowPlayingMovieState by viewModel.nowPlayingMoviesResponses.collectAsState()
    val upcomingMovieState by viewModel.upcomingMoviesResponses.collectAsState()
    val genresMovieState by viewModel.genresMoviesResponses.collectAsState()
    val moviesLazyPagingItems = viewModel.popularAllListState.collectAsLazyPagingItems()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedScreen by remember { mutableStateOf(TvScreen.Home) }

    NavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(modifier = Modifier.padding(16.dp)) {
                TvScreen.entries.forEach { screen ->
                    NavigationDrawerItem(
                        selected = screen == selectedScreen,
                        onClick = {
                            selectedScreen = screen
                            drawerState.setValue(DrawerValue.Closed)
                        },
                        leadingContent = { /* optional icon or spacer */ },
                        content = { Text(text = screen.title) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (selectedScreen) {
                TvScreen.Home -> MainScreen()
                TvScreen.Trending -> TrendingScreen()
                TvScreen.Popular -> PopularScreen()
                TvScreen.Favorite -> FavoriteScreen()
                TvScreen.Settings -> SettingsScreen()
            }
        }
    }

}
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvScreenContent(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFCCCCCC)), // light gray
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )
            Button(
                onClick = { /* handle action */ }
            ) {
                Text("TV Button")
            }
        }
    }
}@Composable
fun MainScreen() {
    TvScreenContent(title = "Home")
}

@Composable
fun TrendingScreen() {
    TvScreenContent(title = "Trending")
}

@Composable
fun PopularScreen() {
    TvScreenContent(title = "Popular")
}

@Composable
fun FavoriteScreen() {
    TvScreenContent(title = "Favorite")
}

@Composable
fun SettingsScreen() {
    TvScreenContent(title = "Settings")
}


enum class TvScreen(val title: String) {
    Home("Home"),
    Trending("Trending"),
    Popular("Popular"),
    Favorite("Favorite"),
    Settings("Settings")
}
