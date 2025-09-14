package com.pixeldev.composetv.screens.favourite

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import androidx.tv.material3.*
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pixeldev.composetv.R
import com.pixeldev.composetv.data.remote.response.GenreResponse
import com.pixeldev.composetv.data.remote.response.MovieResponse
import com.pixeldev.composetv.graph.Screen
import com.pixeldev.composetv.models.Movies
import com.pixeldev.composetv.screens.categories.BorderedFocusableItem
import com.pixeldev.composetv.screens.categories.ErrorScreen
import com.pixeldev.composetv.screens.categories.ErrorStrip
import com.pixeldev.composetv.screens.details.CastSection
import com.pixeldev.composetv.screens.home.GenreSection
import com.pixeldev.composetv.screens.home.HomeViewModel
import com.pixeldev.composetv.screens.home.MovieSection
import com.pixeldev.composetv.utlis.Constants.Companion.BASE_BACKDROP_IMAGE_URL_1280
import com.pixeldev.composetv.utlis.Constants.Companion.BASE_BACKDROP_IMAGE_URL_780
import com.pixeldev.composetv.utlis.Constants.Companion.BASE_POSTER_IMAGE_URL
import com.pixeldev.composetv.utlis.MovieState
import com.pixeldev.composetv.utlis.SectionHeader
import com.pixeldev.composetv.utlis.TVGradientLoadingIndicator
import kotlinx.coroutines.launch

@Composable
fun FavouriteScreen() {
    ImmersiveListScreenNEW(mediaRows)
}


sealed interface RowItem

/**
 * Data class representing an item in our list, with richer details.
 */
data class MediaItem(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val description: String,
    val genres: List<String>,
    val year: String
) : RowItem

/**
 * New data class for a different type of content (e.g., short-form videos).
 */
data class ShortsItem(
    val id: Int,
    val title: String,
    val videoUrl: String,
    val author: String
) : RowItem

/**
 * Data class to represent a row of items, now accepting a generic list of RowItem.
 */
data class MediaRow(
    val id: Int,
    val title: String,
    val items: List<RowItem>
)

/**
 * Dummy data for the immersive list.
 */val mediaItems = listOf(
    MediaItem(
        1,
        "Special Ops",
        "https://picsum.photos/id/1011/500/750",  // Replaced with Picsum
        "When a global tech summit turns violent and a senior Indian intelligence officer is attacked on home soil, Himmat Singh must lead his elite team into a shadow war—fighting a faceless enemy waging silent attacks through systems, secrets, and sabotage—before India falls from within.",
        listOf("Action", "Thriller", "Crime"),
        "2023"
    ),
    MediaItem(
        2,
        "Criminal Justice",
        "https://picsum.photos/id/1027/500/750",  // Replaced with Picsum
        "A one-night stand turns into a nightmare for Aditya, who is charged with the murder of his co-passenger. What happened that night? Is he guilty or not? A new web series that explores the dark underbelly of the Indian criminal justice system.",
        listOf("Crime", "Drama", "Mystery"),
        "2022"
    ),
    MediaItem(
        3,
        "Aarya",
        "https://picsum.photos/id/1035/500/750",  // Replaced with Picsum
        "When her husband is killed, Aarya is pushed into a world of crime. As a drug lord and a single mother, she must find the murderers and protect her children from the criminals and the police.",
        listOf("Crime", "Action", "Thriller"),
        "2024"
    ),
    MediaItem(
        4,
        "Good Luck Jerry",
        "https://picsum.photos/id/1043/500/750",  // Replaced with Picsum
        "A young, timid woman, Jerry, finds herself in a world of crime when she starts working for a drug cartel to make ends meet. She must find her way out and protect her family.",
        listOf("Comedy", "Crime", "Drama"),
        "2022"
    ),
    MediaItem(
        5,
        "A Friday",
        "https://picsum.photos/id/1052/500/750",  // Replaced with Picsum
        "When a young man is found dead, a team of police officers must solve the case. But as they dig deeper, they uncover a web of lies and betrayal.",
        listOf("Thriller", "Crime", "Mystery"),
        "2021"
    )
)


val shortsItems = listOf(
    ShortsItem(10, "Funny Dog Video", "https://picsum.photos/id/237/1920/1080", "Pet Lover"),
    ShortsItem(11, "DIY Crafts", "https://picsum.photos/id/238/1920/1080", "Crafty Hands"),
    ShortsItem(12, "Cooking Tutorial", "https://picsum.photos/id/239/1920/1080", "Chef John")
)

val mediaRows = listOf(
    MediaRow(1, "Latest & Trending", mediaItems),
    MediaRow(2, "Popular Shorts", shortsItems),
    MediaRow(3, "TV Shows", mediaItems.shuffled()),
    MediaRow(4, "New Releases", mediaItems.shuffled())
)

/**
 * The main screen composable that displays an immersive list.
 *
 * @param mediaRows The list of rows to display.
 */
@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ImmersiveListScreenNEW(mediaRows: List<MediaRow>) {
    var focusedItem by remember { mutableStateOf<RowItem?>(mediaRows.firstOrNull()?.items?.firstOrNull()) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image with fade animation
        AnimatedVisibility(
            visible = focusedItem != null,
            enter = fadeIn(animationSpec = tween(durationMillis = 500)),
            exit = fadeOut(animationSpec = tween(durationMillis = 500)),
            modifier = Modifier.fillMaxSize()
        ) {
            val imageUrl = when (val item = focusedItem) {
                is MediaItem -> item.imageUrl
                is ShortsItem -> item.videoUrl
                else -> ""
            }

            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )

            // Gradient overlay for readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 0f,
                            endY = 1000f
                        )
                    )
            )
        }

        // Main scrollable content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hero section
            item {
                focusedItem?.let { item ->
                    ImmersiveHeroSection(item = item)
                }
                Spacer(modifier = Modifier.height(50.dp))
            }

            // Media rows
            items(mediaRows) { mediaRow ->
                // Row title
                Text(
                    text = mediaRow.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 60.dp, vertical = 20.dp)
                )

                // Horizontal list of media items
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 60.dp, vertical = 10.dp)
                ) {
                    items(mediaRow.items) { item ->
                        when (item) {
                            is MediaItem -> {
                                ImmersiveListItemNEW(
                                    item = item,
                                    onFocus = { focusedItem = it }
                                )
                            }

                            is ShortsItem -> {
                                ImmersiveShortsItem(
                                    item = item,
                                    onFocus = { focusedItem = it }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                }
            }
        }

    }
}

/**
 * Displays the large hero section with details based on the item type.
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ImmersiveHeroSection(item: RowItem) {
    Column(
        modifier = Modifier
            .width(600.dp)
            .padding(horizontal = 60.dp)
    ) {
        when (item) {
            is MediaItem -> {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.year,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    item.genres.forEach { genre ->
                        Text(
                            text = genre,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            is ShortsItem -> {
                Text(
                    text = "Shorts: ${item.title}",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "By ${item.author}",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

/**
 * Displays a single card item in the horizontal list.
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ImmersiveListItemNEW(
    item: MediaItem,
    onFocus: (RowItem) -> Unit
) {
    Card(
        onClick = { /* Handle item click */ },
        modifier = Modifier
            .size(width = 250.dp, height = 150.dp)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    onFocus(item)
                }
            },
        colors = CardDefaults.colors(
            containerColor = Color.White.copy(alpha = 0.1f),
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.border(
            focusedBorder = Border(
                BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
                // shape = CardDefaults.shape(isFocused = true)
            )
        )
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

/**
 * Displays a single shorts item card, with a different size.
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ImmersiveShortsItem(
    item: ShortsItem,
    onFocus: (RowItem) -> Unit
) {
    Card(
        onClick = { /* Handle shorts click */ },
        modifier = Modifier
            .size(width = 150.dp, height = 250.dp)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    onFocus(item)
                }
            },
        colors = CardDefaults.colors(
            containerColor = Color.White.copy(alpha = 0.1f),
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.border(
            focusedBorder = Border(
                BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
                // shape = CardDefaults.shape(isFocused = true)
            )
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = item.videoUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentScale = ContentScale.Crop
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
