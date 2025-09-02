package com.pixeldev.composetv.screens.details

/*

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), navController: NavHostController) {
    val showExitDialog = remember { mutableStateOf(false) }
    //Exit Dialog Code
    if (showExitDialog.value) {
        */
/*IncludeApp().showExitDialog {
            showExitDialog.value = false
        }*//*

    }
    BackHandler(enabled = true) {
        if (showExitDialog.value) {
            // Exit the app
            android.os.Process.killProcess(android.os.Process.myPid())
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

        val drawerItemFocusRequesters = remember {
            TvScreen.entries.associateWith { FocusRequester() }
        }

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
                            leadingContent = { },
                            content = { Text(text = screen.title) },
                            modifier = Modifier.focusRequester(drawerItemFocusRequesters[screen]!!)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        ) {
            // 👇 wrap content pages in focus interceptor
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onPreviewKeyEvent { keyEvent ->
                        if (keyEvent.type == KeyEventType.KeyDown &&
                            keyEvent.key == Key.DirectionLeft
                        ) {
                            drawerItemFocusRequesters[selectedScreen]?.requestFocus()
                            true
                        } else false
                    }
                    .focusable()
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
                modifier = Modifier
                    .focusable(),
                onClick = { */
/* handle action *//*
 }

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
*/
