package com.pixeldev.composetv.screens.movie

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
fun MoviesContentScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val discoveryMovieState by viewModel.discoveryMovieResponses.collectAsState()
    val trendingMovieState by viewModel.trendingMovieResponses.collectAsState()
    val nowPlayingMovieState by viewModel.nowPlayingMoviesResponses.collectAsState()
    val upcomingMovieState by viewModel.upcomingMoviesResponses.collectAsState()
    val genresMovieState by viewModel.genresMoviesResponses.collectAsState()
    val moviesLazyPagingItems = viewModel.popularAllListState.collectAsLazyPagingItems()

    val isLoading = listOf(
        discoveryMovieState,
        trendingMovieState,
        nowPlayingMovieState,
        upcomingMovieState,
        genresMovieState
    ).any { it is MovieState.Loading }

    val allSuccess = listOf(
        discoveryMovieState,
        trendingMovieState,
        nowPlayingMovieState,
        upcomingMovieState,
        genresMovieState
    ).all { it is MovieState.Success }

    val hasError = listOf(
        discoveryMovieState,
        trendingMovieState,
        nowPlayingMovieState,
        upcomingMovieState,
        genresMovieState
    ).any { it is MovieState.Error }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                TVGradientLoadingIndicator(modifier = Modifier.align(Alignment.Center))
            }

            hasError -> {
                ErrorScreen(
                    title = "🛠️ Oops! The Hamsters Took a Coffee Break ☕",
                    subtitle = "Our servers are out chasing bugs... or maybe just napping.",
                    message = "We're having a little tech hiccup. Try again in a bit — or bribe the hamsters with snacks!",
                    imageRes = R.drawable.warning // replace with your drawable
                )
            }

            allSuccess -> {
                SetMovieData(
                    discoveryMovie = (discoveryMovieState as MovieState.Success).data,
                    trendingMovie = (trendingMovieState as MovieState.Success).data,
                    nowPlayingMovie = (nowPlayingMovieState as MovieState.Success).data,
                    upcomingMovie = (upcomingMovieState as MovieState.Success).data,
                    genresMovie = (genresMovieState as MovieState.Success).data,
                    popularMovie = moviesLazyPagingItems,
                    navController
                )

            }
        }
    }
}

@Composable
fun SetMovieData(
    discoveryMovie: MovieResponse?,
    trendingMovie: MovieResponse?,
    nowPlayingMovie: MovieResponse?,
    upcomingMovie: MovieResponse?,
    genresMovie: GenreResponse?,
    popularMovie: LazyPagingItems<Movies>,
    navController: NavHostController
) {

    // State to track the focused movie
    val focusedMovie = remember { mutableStateOf<Movies?>(null) }

        val bringIntoViewRequester = remember { BringIntoViewRequester() }
        val coroutineScope = rememberCoroutineScope()
    // Use a LazyColumn to show the dynamic content below the fixed hero section
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .focusable() // 👈 Important!
                    .onFocusEvent { event ->
                        if (event.hasFocus) {
                            coroutineScope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    }
            ) {
                TopHeroSection(focusedMovie.value)
            }
        }


        item {
            Column {


                SectionHeader("Upcoming")
                // Upcoming Movies Row (Dynamic Content)
                LazyRow(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    items(upcomingMovie!!.results.size) { movieIndex ->
                        val movie = upcomingMovie!!.results[movieIndex]
                        // MovieCard for each movie
                        MovieCard(movie = movie) {
                            focusedMovie.value = movie
                        }
                    }
                }
            }
        }

        item {
            MovieSection(
                title = "Discovery Movies",
                movies = discoveryMovie?.results,
                onClickMovieCard = {
                    navController.navigate(Screen.MovieDetails.route)
                })
        }

        item {
            MovieSection(
                title = "Trending Movies",
                movies = trendingMovie?.results,
                onClickMovieCard = { navController.navigate(Screen.MovieDetails.route) }
            )
        }

        item {
            MovieSection(
                title = "Now Playing Movies",
                movies = nowPlayingMovie?.results,
                onClickMovieCard = { navController.navigate(Screen.MovieDetails.route) }
            )
        }

        item {
            Column {
                SectionHeader("Popular Movies")
                ShowPagingMovieList(popularMovie)
            }
        }

        item {
            Column(Modifier.padding(8.dp)) {
                GenreSection(genres = genresMovie?.genres)
            }
        }

    }

}

@Composable
fun ShowPagingMovieList(pagingItem: LazyPagingItems<Movies>) {

    val listState = rememberLazyListState()

    if (pagingItem == null) {
        // Show loader jab tak list null hai
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            TVGradientLoadingIndicator()
        }
    } else {
        LazyRow(
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(pagingItem.itemCount) { i ->
                val movie = pagingItem[i]
                if (movie != null) {
                    BorderedFocusableItem(
                        onClick = {
                            // TODO: handle click
                        },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .width(150.dp)
                            .aspectRatio(0.6f), // Maintains poster ratio
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            AsyncImage(
                                model = BASE_POSTER_IMAGE_URL + (movie.posterPath ?: ""),
                                contentDescription = movie.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .background(Color.Black.copy(alpha = 0.6f))
                                    .padding(6.dp)
                            ) {
                                Text(
                                    text = movie.title ?: "",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                } else {
                    // Placeholder for loading skeleton
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .width(150.dp)
                            .height(225.dp)
                            .background(Color.DarkGray.copy(alpha = 0.3f))
                    ) {
                        TVGradientLoadingIndicator()

                    }
                }
            }

            // Handle Paging Load States
            pagingItem.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item {
                            Box(
                                Modifier
                                    .width(150.dp)
                                    .height(225.dp)
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                TVGradientLoadingIndicator()
                            }
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        item {
                            Box(
                                Modifier
                                    .width(150.dp)
                                    .height(225.dp)
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                TVGradientLoadingIndicator()
                            }
                        }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val e = loadState.refresh as LoadState.Error
                        item {
                            ErrorStrip(
                                message = e.error.localizedMessage ?: "Unknown error"
                            )
                        }
                    }

                    loadState.append is LoadState.Error -> {
                        val e = loadState.append as LoadState.Error
                        item {
                            ErrorStrip(
                                message = e.error.localizedMessage ?: "Unknown error"
                            )
                        }
                    }
                }
            }
        }

    }
}


@Composable
fun TopHeroSection(movie: Movies?) {
    val context = LocalContext.current
    if (movie != null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(BASE_BACKDROP_IMAGE_URL_1280 + movie!!.backdropPath)
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 200f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val rating = movie.voteAverage?.let { String.format("%.1f", it) } ?: "-"
                    val year = movie.releaseDate?.take(4) ?: "-"
                    Text(
                        text = rating,
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = year, color = Color.Gray)
                    Spacer(Modifier.width(8.dp))
                    // Text(text = movie.duration, color = Color.Gray)
                }

                Text(
                    text = movie.title ?: "",
                    style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = movie.overview ?: "",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    } else {
        // 👇 Placeholder when no movie is focused yet
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFF1A1A1A),
                            Color.Black
                        )
                    )
                )
        ) {
            Text(
                text = "Select a movie to preview",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun MovieCard(
    movie: Movies,
    onFocus: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.1f else 1f,
        label = "CardScale"
    )

    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(180.dp, 100.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .onFocusChanged {
                isFocused = it.isFocused
                if (it.isFocused) onFocus()
            }
            .focusable(), onClick = {}
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(BASE_BACKDROP_IMAGE_URL_780 + movie.backdropPath)
                .crossfade(true)
                .build(),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(BASE_BACKDROP_IMAGE_URL_780 + movie.backdropPath)
                .crossfade(true)
                .listener(
                    onStart = {
                        Log.d(
                            "ImageLoad",
                            "⏳ Loading: ${BASE_BACKDROP_IMAGE_URL_780 + movie.backdropPath}"
                        )
                    },
                    onSuccess = { _, _ ->
                        Log.d(
                            "ImageLoad",
                            "✅ Loaded: ${BASE_BACKDROP_IMAGE_URL_780 + movie.backdropPath}"
                        )
                    },
                    onError = { _, result ->
                        Log.e(
                            "ImageLoad",
                            "❌ Error loading: ${BASE_BACKDROP_IMAGE_URL_780 + movie.backdropPath}",
                            result.throwable
                        )
                    }
                )
                .build(),
            contentDescription = "Background Poster",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

    }
}

data class Movie(
    val id: String,
    val title: String,
    val imageUrl: String,
    val category: String,
    val rating: String,
    val year: String,
    val duration: String,
    val description: String
)
