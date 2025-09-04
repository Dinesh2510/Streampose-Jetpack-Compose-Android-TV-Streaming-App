package com.pixeldev.composetv.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Text
import androidx.tv.material3.rememberDrawerState
import com.pixeldev.composetv.screens.home.HomeScreen
import com.pixeldev.composetv.screens.home.Page
import com.pixeldev.composetv.screens.home.PageContent
import com.pixeldev.composetv.screens.home.TvCategoriesScreen
import com.pixeldev.composetv.screens.home.TvScreenContent
import com.pixeldev.composetv.screens.splash.SplashScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.DashBoardScreen.route) {
            DashBoardScreen(navController = navController)
        }
        composable(Screen.UserDetails.route) {

        }
    }
}
@Composable
fun DashBoardScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var selectedPageIndex by remember { mutableIntStateOf(0) }

    val innerNavController = rememberNavController() // ✅ new navController for inner NavHost

    val pages = listOf(
        Page("Home", Icons.Default.Home),
        Page("Search", Icons.Default.Search),
        Page("Favorites", Icons.Default.Favorite),
        Page("Settings", Icons.Default.Settings),
        Page("Profile", Icons.Default.Person)
    )

    NavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxHeight()
                    .padding(16.dp)
                    .selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.Start
            ) {
                pages.forEachIndexed { index, page ->
                    NavigationDrawerItem(
                        selected = selectedPageIndex == index,
                        onClick = {
                            selectedPageIndex = index
                            coroutineScope.launch {
                                drawerState.setValue(DrawerValue.Closed) }
                            // navigate inside the drawer
                            innerNavController.navigate(
                                when (index) {
                                    0 -> Screen.HomeScreen.route
                                    1 -> Screen.SearchScreen.route
                                    2 -> Screen.FavoritesScreen.route
                                    3 -> Screen.SettingsScreen.route
                                    else -> Screen.ProfileScreen.route
                                }
                            )
                        },
                        leadingContent = { Icon(page.icon, contentDescription = page.title) }
                    ) {
                        Text(page.title, color = Color.White)
                    }
                }
            }
        }
    ) {
        NavHost(
            navController = innerNavController,  // ✅ use inner navController
            startDestination = Screen.HomeScreen.route
        ) {
            composable(Screen.HomeScreen.route) {
                HomeScreen(navController = navController) // parent navController for app-level navigation
            }
            composable(Screen.SearchScreen.route) {
                TvCategoriesScreen()
            }
            composable(Screen.FavoritesScreen.route) {
                PageContent(1)
            }
            composable(Screen.SettingsScreen.route) {
                TvScreenContent("SettingsScreen")
            }
            composable(Screen.ProfileScreen.route) {
                TvScreenContent("ProfileScreen")
            }
        }
    }
}


