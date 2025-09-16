package com.pixeldev.composetv.screens.favourite

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import androidx.tv.material3.*
import coil.compose.AsyncImage
import com.pixeldev.composetv.data.local.WatchListModel

@Composable
fun FavouriteScreen(navController: NavHostController) {
    val watchListViewModel: WatchListViewModel = hiltViewModel()

    ImmersiveListScreenNEW(watchListViewModel, navController)
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ImmersiveListItemNEW(
    item: WatchListModel,
    onFocus: (WatchListModel) -> Unit,
    onClick: () -> Unit
) {
    /**
     * Displays a single card item in the horizontal list.
     * ReactItem (wider poster) ->0
     */
    /**
     * Displays a single shorts item card, with a different size.
     * ShortsItem (taller poster) ->1
     */
    var contentType = 1
    var isFocused by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.1f else 1f,
        animationSpec = tween(200),
        label = "focusScale"
    )

    val cardSize = when (contentType) {
        0 -> Modifier.size(width = 250.dp, height = 150.dp) // ReactItem (wider poster)
        1 -> Modifier.size(width = 150.dp, height = 250.dp) // ShortsItem (taller poster)
        else -> Modifier.size(width = 200.dp, height = 200.dp)
    }

    Card(
        onClick = { onClick() },
        modifier = cardSize
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                if (focusState.isFocused) onFocus(item)
            },
        colors = CardDefaults.colors(
            containerColor = Color.White.copy(alpha = 0.05f),
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.border(
            focusedBorder = Border(
                BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
            )
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            var thumbnails = when (contentType) {
                0 -> item.imageBackDropPath
                1 -> item.imagePosterPath
                else -> {
                    item.imagePosterPath
                }
            }
            AsyncImage(
                model = thumbnails,
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Crop
            )

            // Show title only for Shorts (since posters are tall)
            if (contentType == 1) {
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
}
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun NetflixPosterCard(
    movie: WatchListModel,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.1f else 1f,
        animationSpec = tween(200),
        label = "posterScale"
    )

    Card(
        onClick = { onClick() },
        modifier = Modifier
            .width(120.dp)   // Netflix poster width
            .height(180.dp)  // Netflix poster height (2:3 ratio)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        colors = CardDefaults.colors(
            containerColor = Color.Transparent
        ),
        border = CardDefaults.border(
            focusedBorder = Border(
                BorderStroke(3.dp, Color.White)
            )
        )
    ) {
        AsyncImage(
            model = movie.imageBackDropPath,
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ImmersiveListScreenNEW(
    watchListViewModel: WatchListViewModel,
    controller: NavHostController
) {
    var contentType = 1
    val roomData = watchListViewModel.myMovieData.value.collectAsState(initial = emptyList()).value
    var focusedMovie by remember { mutableStateOf<WatchListModel?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        AnimatedVisibility(
            visible = focusedMovie != null,
            enter = fadeIn(tween(500)),
            exit = fadeOut(tween(500)),
            modifier = Modifier.fillMaxSize()
        ) {
            val imageUrl = focusedMovie?.imageBackDropPath ?: ""
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
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

        // Main content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                focusedMovie?.let { movie ->
                    ImmersiveHeroSection(item = movie, contentType)
                }
                Spacer(modifier = Modifier.height(50.dp))
            }

            if (roomData.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Data in WatchList", color = Color.White)
                    }
                }
            } else {
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 60.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(roomData, key = { it.mediaId }) { movie ->
                            ImmersiveListItemNEW(
                                item = movie,
                                onFocus = { focusedMovie = it },
                                onClick = {
                                    controller.navigate("movieDetails/${movie.mediaId}")
                                })
                        }
                    }
                }
            }
        }
    }
}


/**
 * Displays the large hero section with details based on the item type.
 */
/**
 * Displays the large hero section with details based on the WatchListModel type.
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ImmersiveHeroSection(item: WatchListModel, contentType: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        when (contentType) {
            0 -> { // ReactItem (Movies/Series)
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    /* item.genres?.forEach { genre ->
                         Text(
                             text = genre,
                             style = MaterialTheme.typography.labelMedium,
                             color = Color.White.copy(alpha = 0.8f)
                         )
                         Spacer(modifier = Modifier.width(5.dp))
                     }*/
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = item.title ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            1 -> { // ShortsItem
                Text(
                    text = " ${item.title}",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            else -> {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )
            }
        }
    }
}




