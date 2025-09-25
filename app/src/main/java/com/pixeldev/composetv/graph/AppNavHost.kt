package com.pixeldev.composetv.graph

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Text
import androidx.tv.material3.rememberDrawerState
import com.pixeldev.composetv.screens.categories.CategoryDetails
import com.pixeldev.composetv.screens.categories.CategoryScreen
import com.pixeldev.composetv.screens.details.MovieDetailsScreen
import com.pixeldev.composetv.screens.favourite.FavouriteScreen
import com.pixeldev.composetv.screens.home.HomeScreen
import com.pixeldev.composetv.screens.home.Page
import com.pixeldev.composetv.screens.home.TvScreenContent
import com.pixeldev.composetv.screens.login.LoginScreen
import com.pixeldev.composetv.screens.movie.MoviesContentScreen
import com.pixeldev.composetv.screens.search.SearchScreen
import com.pixeldev.composetv.screens.setting.SettingScreen
import com.pixeldev.composetv.screens.shows.AllShowsScreen
import com.pixeldev.composetv.screens.splash.SplashScreen
import com.pixeldev.composetv.screens.webview.MyWebViewScreen
import com.pixeldev.composetv.utlis.FullScreenDialog
import kotlinx.coroutines.launch
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    val Movie_Details_ID = "movieId"

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.DashBoardScreen.route) {
            DashBoardScreen(navController = navController)
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(
            route = Screen.MovieDetails.route + "/{$Movie_Details_ID}",
            arguments = listOf(navArgument(Movie_Details_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString(Movie_Details_ID) ?: return@composable

            MovieDetailsScreen(
                navController,
                movieId = movieId,
                onBackPressed = { navController.popBackStack() },
            )
        }

        composable(Screen.CategoryDetailsScreen.route) {
            CategoryDetails(navController)
        }
        // WebView Screen with Parameters
        composable(
            route = Screen.WebViewScreen.route
        ) { backStackEntry ->
            val webTitle = backStackEntry.arguments?.getString("webTitle") ?: "Default Title"
            val webUrl = backStackEntry.arguments?.getString("webUrl") ?: "https://www.example.com"
            MyWebViewScreen(webTitle, webUrl, navController)
        }
    }
}


@Composable
fun DashBoardScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val innerNavController = rememberNavController()

    val pages = listOf(
        Page("Search", Icons.Default.Search),
        Page("Home", Icons.Default.Home),
        Page("Categories", Icons.Default.Category),
        Page("Movies", Icons.Default.Movie),
        Page("Shows", Icons.Default.Slideshow),
        Page("Favorite", Icons.Default.Favorite),
        Page("Settings", Icons.Default.Settings),
    )

    // --- Track current route for highlight sync ---
    val currentBackStack by innerNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    var selectedPageIndex by remember { mutableIntStateOf(1) }

    /*    val selectedPageIndex = pages.indexOfFirst { page ->
        when (page.title) {
            "Search" -> currentRoute == Screen.SearchScreen.route
            "Home" -> currentRoute == Screen.HomeScreen.route
            "Categories" -> currentRoute == Screen.CategoriesScreen.route
            "Movies" -> currentRoute == Screen.MoviesScreen.route
            "Shows" -> currentRoute == Screen.ShowsScreen.route
            "Favorite" -> currentRoute == Screen.FavoritesScreen.route
            "Settings" -> currentRoute == Screen.SettingsScreen.route
            else -> false
        }
    }.takeIf { it != -1 } ?: 1 // fallback to Home if unknown
*/
// Sync with currentRoute NEW 😎
    LaunchedEffect(currentRoute) {
        if (currentRoute == Screen.HomeScreen.route) {
            // This block runs whenever HomeScreen becomes active
            Log.d("DashBoard", "HomeScreen is now active")
        }
        selectedPageIndex = when (currentRoute) {
            Screen.SearchScreen.route -> 0
            Screen.HomeScreen.route -> 1
            Screen.CategoriesScreen.route -> 2
            Screen.MoviesScreen.route -> 3
            Screen.ShowsScreen.route -> 4
            Screen.FavoritesScreen.route -> 5
            Screen.SettingsScreen.route -> 6
            else -> 1
        }
    }
    DisposableEffect(Unit) {
        Log.d("DashBoard", "DrawerContent composed")
        onDispose {
            Log.d("DashBoard", "DrawerContent disposed")
        }
    }

    // --- Focus Requesters for TV focus restore ---
    val focusRequesters = remember { pages.map { FocusRequester() } }

    LaunchedEffect(drawerState.currentValue, selectedPageIndex) {
        if (drawerState.currentValue == DrawerValue.Open) {
            focusRequesters[selectedPageIndex].requestFocus()
            Log.d("DashBoard", "Focus restored on $currentRoute")
        }
    }

    NavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxHeight()
                    .padding(16.dp)
                    .selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.Start
            ) {
                pages.forEachIndexed { index, page ->
                    NavigationDrawerItem(
                        modifier = Modifier.focusRequester(focusRequesters[index]),
                        selected = selectedPageIndex == index,
                        onClick = {
                            selectedPageIndex = index // immediately update state 😎
                            coroutineScope.launch { drawerState.setValue(DrawerValue.Closed) }
                            innerNavController.navigate(
                                when (index) {
                                    0 -> Screen.SearchScreen.route
                                    1 -> Screen.HomeScreen.route
                                    2 -> Screen.CategoriesScreen.route
                                    3 -> Screen.MoviesScreen.route
                                    4 -> Screen.ShowsScreen.route
                                    5 -> Screen.FavoritesScreen.route
                                    else -> Screen.SettingsScreen.route
                                }
                            ) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(Screen.HomeScreen.route) { saveState = true }
                            }
                        },
                        leadingContent = { Icon(page.icon, contentDescription = page.title) }
                    ) {
                        Text(page.title)
                    }
                }
            }
        }
    ) {
        NavHost(
            navController = innerNavController,
            startDestination = Screen.HomeScreen.route
        ) {
            composable(Screen.HomeScreen.route) {
                HomeScreen(navController = navController) // parent navController
            }
            composable(Screen.SearchScreen.route) {
                SearchScreen(navController)
            }
            composable(Screen.FavoritesScreen.route) {
                FavouriteScreen(navController)
            }
            composable(Screen.SettingsScreen.route) {
                SettingScreen(navController)
            }
            composable(Screen.ProfileScreen.route) {
                TvScreenContent("ProfileScreen")
            }
            composable(Screen.ShowsScreen.route) {
                AllShowsScreen(navController)
            }
            composable(Screen.MoviesScreen.route) {
                MoviesContentScreen(navController = navController)
            }
            composable(Screen.CategoriesScreen.route) {
                CategoryScreen(navController)
            }
        }
    }

    // --- BackHandler logic ---
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        if (currentRoute == Screen.HomeScreen.route) {
            showExitDialog = true
        } else {
            innerNavController.navigate(Screen.HomeScreen.route) {
                popUpTo(Screen.HomeScreen.route) { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    if (showExitDialog) {
        FullScreenDialog(
            title = "Exit app?",
            description = "Are you sure you want to exit?",
            confirmButtonText = "Yes, Exit",
            cancelButtonText = "Cancel",
            onConfirm = {
                showExitDialog = false
                (context as? Activity)?.finish()
            },
            onCancel = {
                showExitDialog = false
            }
        )
    }
}


