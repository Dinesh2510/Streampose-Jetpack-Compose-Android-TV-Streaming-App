package com.pixeldev.composetv.screens.setting

import androidx.annotation.FloatRange
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.pixeldev.composetv.utlis.rememberChildPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Support
import androidx.compose.material.icons.filled.Translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.pixeldev.composetv.R
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class SettingsTab(
    val route: String,
    @Contextual val icon: ImageVector,
    val title: String
)
object Routes {
    const val ACCOUNTS = "accounts"
    const val ABOUT = "about"
    const val SUBTITLES = "subtitles"
    const val LANGUAGE = "language"
    const val SEARCH_HISTORY = "search_history"
    const val HELP_AND_SUPPORT = "help_and_support"
}

// List of settings tabs
val settingsTabs = listOf(
    SettingsTab(route = "accounts", icon = Icons.Default.Person, title = "Accounts"),
    SettingsTab(route = "about", icon = Icons.Default.Info, title = "About"),
    SettingsTab(route = "subtitles", icon = Icons.Default.Subtitles, title = "Subtitles"),
    SettingsTab(route = "language", icon = Icons.Default.Translate, title = "Language"),
    SettingsTab(route = "search_history", icon = Icons.Default.Search, title = "Search History"),
    SettingsTab(
        route = "help_and_support",
        icon = Icons.Default.Support,
        title = "Help and Support"
    )
)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingScreen(
    navHostController: NavHostController,
    @FloatRange(from = 0.0, to = 1.0)
    sidebarWidthFraction: Float = 0.32f
) {
    val childPadding = rememberChildPadding()
    val profileNavController = rememberNavController()

    val backStack by profileNavController.currentBackStackEntryAsState()
    val currentDestination =
        remember(backStack?.destination?.route) { backStack?.destination?.route }
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
                        selected = currentDestination == profileScreen.route,
                        onClick = {
                            focusManager.moveFocus(FocusDirection.Right)
                            // Profile navigation now uses Routes constants
                            /*profileNavController.navigate(profileScreen.route) {
                                currentDestination?.let { nnCurrentDestination ->
                                    popUpTo(nnCurrentDestination) { inclusive = true }
                                }
                                launchSingleTop = true
                            }*/
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (index == 0) Modifier.focusRequester(focusRequester)
                                else Modifier
                            )
                            .onFocusChanged {
                                if (it.isFocused && currentDestination != profileScreen.route) {
                                    profileNavController.navigate(profileScreen.route) { // Use Routes constants
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
            startDestination = Routes.ACCOUNTS,  // Now using Routes constant
            builder = {
                composable(Routes.ACCOUNTS) { AccountsSection(navHostController) }
                composable(Routes.ABOUT) { AboutSection() }
                composable(Routes.SUBTITLES) {
                    SubtitlesSection(
                        isSubtitlesChecked = isSubtitlesChecked,
                        onSubtitleCheckChange = { isSubtitlesChecked = it }
                    )
                }
                composable(Routes.LANGUAGE) {
                    LanguageSection(
                        selectedIndex = selectedLanguageIndex,
                        onSelectedIndexChange = { selectedLanguageIndex = it }
                    )
                }
                composable(Routes.SEARCH_HISTORY) { SearchHistorySection() }
                composable(Routes.HELP_AND_SUPPORT) { HelpAndSupportSection(navHostController) }
            }
        )
    }
}
