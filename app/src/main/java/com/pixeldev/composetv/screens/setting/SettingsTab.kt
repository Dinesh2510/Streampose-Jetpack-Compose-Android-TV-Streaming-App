/*
@Serializable
data class SettingsTab(
    val route: String,
    @Contextual val icon: ImageVector,
    val title: String
)
// List of settings tabs
val settingsTabs = listOf(
    SettingsTab(route = "accounts", icon = Icons.Default.Person, title = "Accounts"),
    SettingsTab(route = "about", icon = Icons.Default.Info, title = "About"),
    SettingsTab(route = "subtitles", icon = Icons.Default.Subtitles, title = "Subtitles"),
    SettingsTab(route = "language", icon = Icons.Default.Translate, title = "Language"),
    SettingsTab(route = "search_history", icon = Icons.Default.Search, title = "Search History"),
    SettingsTab(route = "help_and_support", icon = Icons.Default.Support, title = "Help and Support")
)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingScreen(
    @FloatRange(from = 0.0, to = 1.0)
    sidebarWidthFraction: Float = 0.32f
) {
    val childPadding = rememberChildPadding()
    val profileNavController = rememberNavController()

    val backStack by profileNavController.currentBackStackEntryAsState()
    val currentDestination = remember(backStack?.destination?.route) { backStack?.destination?.route }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isLeftColumnFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = childPadding.start, vertical = childPadding.top)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(fraction = sidebarWidthFraction)
                .verticalScroll(rememberScrollState())
                .fillMaxHeight()
                .onFocusChanged {
                    isLeftColumnFocused = it.hasFocus
                }
                .focusRestorer()
                .focusGroup(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            settingsTabs.forEachIndexed { index, profileScreen ->
                key(index) {
                    ListItem(
                        trailingContent = {
                            Icon(
                                profileScreen.icon,
                                modifier = Modifier
                                    .padding(vertical = 2.dp)
                                    .padding(start = 4.dp)
                                    .size(20.dp),
                                contentDescription = stringResource(
                                    id = R.string.app_name,
                                    profileScreen.title
                                )
                            )
                        },
                        headlineContent = {
                            Text(
                                text = profileScreen.title,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        selected = currentDestination == profileScreen.route, // Use `route`
                        onClick = {
                            focusManager.moveFocus(FocusDirection.Right)
                            profileNavController.navigate(profileScreen.route) { // Use `profileScreen.route`
                                currentDestination?.let { nnCurrentDestination ->
                                    popUpTo(nnCurrentDestination) { inclusive = true }
                                }
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (index == 0) Modifier.focusRequester(focusRequester)
                                else Modifier
                            )
                            .onFocusChanged {
                                if (it.isFocused && currentDestination != profileScreen.route) {
                                    profileNavController.navigate(profileScreen.route) { // Use `profileScreen.route`
                                        currentDestination?.let { nnCurrentDestination ->
                                            popUpTo(nnCurrentDestination) { inclusive = true }
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            },
                        scale = ListItemDefaults.scale(focusedScale = 1f),
                        colors = ListItemDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.inverseSurface,
                            selectedContainerColor = MaterialTheme.colorScheme.inverseSurface
                                .copy(alpha = 0.4f),
                            selectedContentColor = MaterialTheme.colorScheme.surface,
                        ),
                        shape = ListItemDefaults.shape(shape = MaterialTheme.shapes.extraSmall)
                    )
                }
            }
        }

        var selectedLanguageIndex by rememberSaveable { mutableIntStateOf(0) }
        var isSubtitlesChecked by rememberSaveable { mutableStateOf(true) }

        // Single NavHost for all your settings screens
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .onPreviewKeyEvent {
                    if (it.key == Key.Back && it.type == KeyEventType.KeyUp) {
                        while (!isLeftColumnFocused) {
                            focusManager.moveFocus(FocusDirection.Left)
                        }
                        return@onPreviewKeyEvent true
                    }
                    false
                },
            navController = profileNavController,
            startDestination = "accounts",  // Correct start destination
            builder = {
                composable("accounts") { AccountsSection() }
                composable("about") { AboutSection() }
                composable("subtitles") {
                    SubtitlesSection(
                        isSubtitlesChecked = isSubtitlesChecked,
                        onSubtitleCheckChange = { isSubtitlesChecked = it }
                    )
                }
                composable("language") {
                    LanguageSection(
                        selectedIndex = selectedLanguageIndex,
                        onSelectedIndexChange = { selectedLanguageIndex = it }
                    )
                }
                composable("search_history") { SearchHistorySection() }
                composable("help_and_support") { HelpAndSupportSection() }
            }
        )
    }
}

@Preview(device = Devices.TV_1080p)
@Composable
fun ProfileScreenPreview() {
  /*  JetStreamTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            ProfileScreen()
        }
    }*/
}
*/