package com.pixeldev.composetv.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ModalNavigationDrawer
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Text
import androidx.tv.material3.rememberDrawerState
import androidx.tv.material3.*
import coil.compose.rememberAsyncImagePainter
import com.pixeldev.composetv.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreenTwo(viewModel: HomeViewModel = hiltViewModel(), navController: NavHostController) {
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


    // SampleModalNavigationDrawerWithGradientScrim()
    TvApp()
    // initialize focus reference to be able to request focus programmatically

}

@Composable
fun TvAppSimpleFocus() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var selectedPageIndex by remember { mutableStateOf(0) }

    val drawerFocusRequester = remember { FocusRequester() }
    val contentFocusRequester = remember { FocusRequester() }

    val pages = listOf(
        Page("Home", Icons.Default.Home),
        Page("Search", Icons.Default.Search),
        Page("Favorites", Icons.Default.Favorite),
        Page("Settings", Icons.Default.Settings),
        Page("Profile", Icons.Default.Person)
    )

    // This box handles Back key to move focus from content to drawer
    Box(
        Modifier
            .fillMaxSize()
            .onPreviewKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Back) {
                    // If drawer is closed, open drawer and move focus there
                    if (drawerState.currentValue == DrawerValue.Closed) {
                        coroutineScope.launch {
                            drawerState.setValue(DrawerValue.Open)
                            drawerFocusRequester.requestFocus()
                        }
                        true
                    } else false
                } else false
            }
    ) {
        NavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                Column(
                    Modifier
                        .background(Color.DarkGray)
                        .fillMaxHeight()
                        .padding(16.dp)
                        .selectableGroup(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    pages.forEachIndexed { index, page ->
                        NavigationDrawerItem(
                            modifier = if (index == selectedPageIndex) Modifier.focusRequester(
                                drawerFocusRequester
                            ) else Modifier,
                            selected = selectedPageIndex == index,
                            onClick = {
                                selectedPageIndex = index
                                coroutineScope.launch {
                                    drawerState.setValue(DrawerValue.Closed)

                                    // Move focus to first card after drawer closes
                                    delay(300) // wait for drawer close animation
                                    contentFocusRequester.requestFocus()
                                }
                            },
                            leadingContent = {
                                Icon(page.icon, contentDescription = page.title)
                            }
                        ) {
                            Text(page.title, color = Color.White)
                        }
                    }
                }
            }
        ) {
            PageContentSimple(
                pageIndex = selectedPageIndex,
                contentFocusRequester = contentFocusRequester
            )
        }
    }
}

@Composable
fun PageContentSimple(pageIndex: Int, contentFocusRequester: FocusRequester) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(100) { index ->
            Card(
                modifier = if (index == 0)
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .focusRequester(contentFocusRequester)
                        .focusable()
                else Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .focusable(), onClick = {}
            ) {
                Column {
                    Text(
                        text = "Page ${pageIndex + 1} – Item ${index + 1}",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = "https://picsum.photos/seed/$pageIndex-$index/400/200"
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TvApp() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var selectedPageIndex by remember { mutableIntStateOf(0) }

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
                            coroutineScope.launch { drawerState.setValue(DrawerValue.Closed) }
                        },
                        leadingContent = {
                            Icon(page.icon, contentDescription = page.title)
                        }
                    ) {
                        Text(page.title, color = Color.White)
                    }
                }
            }
        }
    ) {
        //PageContent(selectedPageIndex)
        TvCategoriesScreen()
    }
}

@Composable
fun PageContent(pageIndex: Int) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(100) { index ->
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .height(200.dp), // adjust as needed,
                onClick = {}
            ) {
                Column {
                    Text(
                        text = "Page ${pageIndex + 1} – Item ${index + 1}",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = "https://picsum.photos/seed/$pageIndex-$index/400/200"
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )
                }
            }
        }
    }
}


data class Page(val title: String, val icon: ImageVector)

@OptIn(ExperimentalTvMaterial3Api::class)


@Composable
fun SampleModalNavigationDrawerWithGradientScrim() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val items =
        listOf(
            "Home" to Icons.Default.Home,
            "Trending" to Icons.Default.LocalFireDepartment,
            "Popular" to Icons.AutoMirrored.Filled.TrendingUp,
            "Favourites" to Icons.Default.Favorite,
            "Settings" to Icons.Default.Settings,
        )
    val closeDrawerWidth = 80.dp
    val backgroundContentPadding = 10.dp
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxHeight()
                    .padding(12.dp)
                    .selectableGroup(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NavigationDrawerItem(selected = false, onClick = {

                }, leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Person ?: return@NavigationDrawerItem,
                        contentDescription = "",
                        modifier = Modifier.size(40.dp),
                    )
                }) {
                    Text("Profile")
                }
                Spacer(modifier = Modifier.height(16.dp))
                items.forEachIndexed { index, item ->
                    val (text, icon) = item
                    NavigationDrawerItem(
                        selected = selectedIndex == index,
                        onClick = {
                            drawerState.setValue(DrawerValue.Closed)
                            selectedIndex = index

                        },
                        leadingContent = {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                            )
                        }
                    ) {
                        Text(text)
                    }
                }
            }
        },
        scrimBrush = Brush.horizontalGradient(listOf(Color.Black, Color.Transparent))
    ) {
        // Content Area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = closeDrawerWidth + backgroundContentPadding)
        ) {
            TvCategoriesScreen()

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
}

@Composable
fun MainScreen() {
    val focusRequesterA = remember { FocusRequester() }
    val focusRequesterB = remember { FocusRequester() }

    Row {
        Button(
            onClick = { /* do something */ },
            modifier = Modifier
                .focusRequester(focusRequesterA)
                .focusProperties {
                    right = focusRequesterB
                }
        ) {
            Text("A")
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = { /* do something */ },
            modifier = Modifier
                .focusRequester(focusRequesterB)
                .focusProperties {
                    left = focusRequesterA
                }
        ) {
            Text("B")
        }
    }

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


/*val focusRequester = remember { FocusRequester() }
// MutableInteractionSource to track changes of the component's interactions (like "focused")
val interactionSource = remember { MutableInteractionSource() }

// text below will change when we focus it via button click
val isFocused = interactionSource.collectIsFocusedAsState().value
val text =
    if (isFocused) {
        "Focused! tap anywhere to free the focus"
    } else {
        "Bring focus to me by tapping the button below!"
    }
Column {
    // this Text will change it's text parameter depending on the presence of a focus
    Text(
        text = text,
        modifier =
            Modifier
                // add focusRequester modifier before the focusable (or even in the parent)
                .focusRequester(focusRequester)
                .focusable(interactionSource = interactionSource),
    )
    Button(onClick = { focusRequester.requestFocus() }) {
        Text("Bring focus to the text above")
    }
}*/


@Composable
fun TvCategoriesScreen() {
    val categories = (1..10).map { "Category $it" }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(categories.size) { category ->
            Text(
                text = categories[category],
                color = Color.White,
                modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
            )
            Spacer(Modifier.height(8.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp), // Or remove this line
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp), // Control padding here
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(20) { index ->
                    TvCardItem(
                        title = "$category - Item ${index + 1}",
                        imageUrl = "https://picsum.photos/seed/${category.hashCode()}$index/300/200"
                    )
                }
            }

        }
    }
}

@Composable
fun TvCardItem(
    title: String,
    imageUrl: String
) {
    CompactCard(
        onClick = { },
        modifier = Modifier
            .width(180.dp),
        image = {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(CardDefaults.HorizontalImageAspectRatio),
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        },
        title = {
            Text(
                text = title,
                modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
            )
        },
        subtitle = {
            Text(
                text = "Secondary · text",
                modifier = Modifier.padding(
                    top = 4.dp,
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 8.dp
                )
            )
        },
    )
    /*  Card(
          onClick = {},
          modifier = Modifier.width(200.dp).aspectRatio(CardDefaults.HorizontalImageAspectRatio),
          border =
              CardDefaults.border(
                  focusedBorder =
                      Border(
                          border = BorderStroke(width = 3.dp, color = Color.White),
                          shape = RoundedCornerShape(5),
                      ),
              ),
          colors =
              CardDefaults.colors(containerColor = Color.Gray, focusedContainerColor = Color.Gray),
          scale =
              CardDefaults.scale(
                  focusedScale = 1.05f,
              )
      ) {  Column {
          Image(
              painter = rememberAsyncImagePainter(imageUrl),
              contentDescription = title,
              modifier = Modifier
                  .fillMaxWidth()
                  .height(120.dp),
              contentScale = ContentScale.Crop
          )
          Text(
              text = title,
              color = Color.White,
              style = MaterialTheme.typography.bodyMedium,
              modifier = Modifier
                  .padding(8.dp)
                  .fillMaxWidth(),
              maxLines = 1
          )
      }}*/

}

