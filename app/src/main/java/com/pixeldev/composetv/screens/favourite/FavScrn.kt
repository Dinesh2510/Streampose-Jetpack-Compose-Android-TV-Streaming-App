package com.pixeldev.composetv.screens.favourite

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import androidx.tv.material3.*
import coil.compose.AsyncImage

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun NetflixImmersiveScreen(
    title: String,
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit
) {
    // Track currently focused movie
    var selectedMovie by remember { mutableStateOf(movies.firstOrNull()) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image (blurred or dimmed backdrop)
        selectedMovie?.let { movie ->
            AsyncImage(
                model = movie.backdropUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Black),
                            startY = 300f
                        )
                    )
            )
        }

        // Main content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp)
        ) {
            // Hero section (big banner)
            item {
                selectedMovie?.let { movie ->
                    NetflixHeroSection(movie = movie)
                }
                Spacer(modifier = Modifier.height(40.dp))
            }

            // Movie row
            item {
                NetflixRow(
                    title = title,
                    movies = movies,
                    onClick = { onMovieClick(it) },
                    onFocusMovie = { focusedMovie ->
                        selectedMovie = focusedMovie
                    }
                )
            }
        }
    }
}

@Composable
fun NetflixHeroSection(movie: Movie) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp, end = 40.dp, top = 60.dp)
    ) {
        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${movie.year} • ⭐ ${movie.rating}",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = movie.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun NetflixRow(
    title: String,
    movies: List<Movie>,
    onClick: (Movie) -> Unit,
    onFocusMovie: (Movie) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.padding(start = 24.dp, bottom = 12.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(movies) { movie ->
                NetflixPosterCard(
                    movie = movie,
                    onClick = { onClick(movie) },
                    onFocus = { onFocusMovie(movie) }
                )
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun NetflixPosterCard(
    movie: Movie,
    onClick: () -> Unit,
    onFocus: () -> Unit
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
            .width(120.dp)
            .height(180.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                if (focusState.isFocused) {
                    onFocus()
                }
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
            model = movie.posterUrl,
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@kotlinx.serialization.Serializable
data class Movie(
    val id: Int,
    val title: String,
    val year: String,
    val rating: Double,
    val description: String,
    val posterUrl: String,
    val backdropUrl: String
)
