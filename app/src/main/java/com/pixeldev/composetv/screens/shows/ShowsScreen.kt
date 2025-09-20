package com.pixeldev.composetv.screens.shows

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Carousel
import androidx.tv.material3.CarouselDefaults
import androidx.tv.material3.CarouselState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.StandardCardContainer
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pixeldev.composetv.data.remote.response.GenreResponse
import com.pixeldev.composetv.data.remote.response.MovieResponse
import com.pixeldev.composetv.graph.Screen
import com.pixeldev.composetv.models.Genre
import com.pixeldev.composetv.models.Movies
import com.pixeldev.composetv.screens.home.GenreItem
import com.pixeldev.composetv.screens.movie.Movie
import com.pixeldev.composetv.utlis.Constants.Companion.BASE_BACKDROP_IMAGE_URL_780
import com.pixeldev.composetv.utlis.GenreWithSubtitle
import com.pixeldev.composetv.utlis.MovieState
import com.pixeldev.composetv.utlis.SectionHeader
import com.pixeldev.composetv.utlis.TVGradientLoadingIndicator
import com.pixeldev.composetv.utlis.tvGenresWithSubtitles

@Composable
fun AllShowsScreen(
    navHostController: NavHostController,
    viewModel: ShowViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchTrendingTvShows()
        viewModel.fetchAllTvShows() // pass a filmId to get recommended shows
    }

    val categoriesState by viewModel.categoriesState.collectAsState()
    val trendingTvShowsState by viewModel.trendingTvShows.collectAsState()

    LazyColumn {
        item {
            when (trendingTvShowsState) {
                is MovieState.Error -> {

                }

                MovieState.Loading -> {
                    Box {
                        TVGradientLoadingIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }

                is MovieState.Success<*> -> {
                    val data = (trendingTvShowsState as MovieState.Success<MovieResponse?>).data
                    if (data is MovieResponse) {
                        HeroCarousel(heroList = data.results)
                    }
                }
            }
        }
        categoriesState.forEach { (category, state) ->
            item {
                SectionHeader(category)
            }

            when (state) {
                is MovieState.Loading -> {
                    item {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            TVGradientLoadingIndicator()
                        }
                    }
                }

                is MovieState.Error -> {
                    item {
                        Text(
                            text = "Error: ${state.message}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                is MovieState.Success<*> -> {
                    val data = state.data

                    if (data is MovieResponse) {
                        // Horizontal list of TV shows
                        item {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 0.dp), // Or remove this line
                                contentPadding = PaddingValues(
                                    start = 24.dp,
                                    end = 24.dp
                                ), // Control padding here
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(data.results) { showInt ->
                                    // TODO: Shows Details Page API
                                    TVShowCardContainerUI(showInt){
                                        navHostController.navigate(Screen.MovieDetails.route + "/${it}")
                                    }
                                }
                            }
                        }
                    } else if (data is GenreResponse) {
                        item {
                            val enrichedList: List<GenreWithSubtitle> = data.genres.map { it.toWithSubtitle() }

                            val enriched = data.genres.map { it.toWithSubtitle() }
                            val genres: List<Genre> = data.genres

                            val enrichedGenres: List<GenreWithSubtitle> = genres.map { genre ->
                                genre.toWithSubtitle()
                            }

                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 0.dp), // Or remove this line
                                contentPadding = PaddingValues(
                                    start = 24.dp,
                                    end = 24.dp
                                ), // Control padding here
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(enrichedGenres) { genre ->
                                    GenreItem(
                                        genre = genre,
                                        modifier = Modifier,
                                        onClick = { }
                                    )
                                }
                            }

                        }
                    } else {
                        item {
                            Text("Unknown data for $category")
                        }
                    }
                }
            }
        }
    }

}
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun GenreItem(
    modifier: Modifier = Modifier,
    genre: GenreWithSubtitle,
    onClick: () -> Unit
) {
    val gradiantColors = arrayOf(
        .6f to MaterialTheme.colorScheme.surfaceVariant,
        1f to Color.Transparent
    )
    Card(
        colors = CardDefaults.colors(Color.Transparent),
        onClick = onClick,
    ) {
        Box(
            modifier = Modifier.clip(MaterialTheme.shapes.small),
            contentAlignment = Alignment.CenterStart
        ) {
            AsyncImage(
                modifier = modifier
                    .size(280.dp, 80.dp)
                    .drawWithContent {
                        drawContent()
                        drawRect(Brush.horizontalGradient(colorStops = gradiantColors))
                    },
                model = genre.name,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    modifier = Modifier.width(180.dp),
                    text = genre.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.width(200.dp),
                    text = genre.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

fun Genre.toWithSubtitle(): GenreWithSubtitle {
    return tvGenresWithSubtitles.find { it.id == this.id }
        ?: GenreWithSubtitle(id!!, name, "Top ${name} TV Shows")
}

@Composable
fun TVShowCardContainerUI(data: Movies,onClickMovieCard: (movieId: Int) -> Unit) {
    StandardCardContainer(
        modifier = Modifier
            .width(200.dp)
            .padding(horizontal = 4.dp),
        imageCard = {
            Card(
                onClick = { onClickMovieCard(data.id!!)},
                interactionSource = it,
                colors = CardDefaults.colors(containerColor = Color.Transparent)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(BASE_BACKDROP_IMAGE_URL_780 + data.backdropPath)
                        .crossfade(true)
                        .build(),
                    contentDescription = data.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(CardDefaults.HorizontalImageAspectRatio),
                )
            }
        },
        title = {
            Text(
                text = data.name ?: "",
                maxLines = 1,
                modifier = Modifier.padding(top = 8.dp)
            )
        },
        subtitle = {
            val rating = data.voteAverage?.let { String.format("%.1f", it) } ?: "-"
            val year = data.first_air_date?.take(4) ?: "-"

            Text(
                text = "⭐ $rating · $year",
            )
        },
    )
}

@Composable
fun ShowsScreen(
    heroList: List<Movie>,
    recommendedFilms: List<Movie>,
    continueWatching: List<Movie>,
    recommendedSeries: List<Movie>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentPadding = PaddingValues(vertical = 0.dp)
    ) {

        item {
            HorizontalMovieRow(title = "Recommended Films", movies = recommendedFilms)
        }
        item {
            HorizontalMovieRow(title = "Continue Watching", movies = continueWatching)
        }
        item {
            HorizontalMovieRow(title = "Recommended Series", movies = recommendedSeries)
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HeroCarousel(heroList: ArrayList<Movies>) {
    val carouselState = remember { CarouselState(initialActiveItemIndex = 0) }

    Carousel(
        itemCount = heroList.take(5).size,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),  // adjust height as needed
        carouselState = carouselState,
        autoScrollDurationMillis = CarouselDefaults.TimeToDisplayItemMillis,  // default or custom
        contentTransformStartToEnd = fadeIn(tween(durationMillis = 1000)).togetherWith(
            fadeOut(tween(durationMillis = 1000))
        ),
        contentTransformEndToStart = fadeIn(tween(durationMillis = 1000)).togetherWith(
            fadeOut(tween(durationMillis = 1000))
        ),
        carouselIndicator = {
            // default indicator; can customize dots or style
            CarouselDefaults.IndicatorRow(
                itemCount = heroList.take(5).size,
                activeItemIndex = carouselState.activeItemIndex,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }
    ) { index ->
        val movie = heroList[index]
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            AsyncImage(
                model = BASE_BACKDROP_IMAGE_URL_780 + movie.backdropPath,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Overlay text etc
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,  // start of the gradient
                                Color(0xFF261F1F)  // Dark Navy or your chosen color at the bottom
                            ),
                            startY = 0f,
                            endY = 200f  // Adjust height of the gradient
                        )
                    )
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = movie.name ?: "",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.overview ?: "",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "⭐ 4.5 · 2025",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun HorizontalMovieRow(title: String, movies: List<Movie>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            modifier = Modifier.padding(start = 32.dp, top = 8.dp),
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        TvLazyRow(movies = movies)
    }
}

@Composable
fun TvLazyRow(movies: List<Movie>) {
    // tvFoundation provides TvLazyRow etc, or you can use LazyRow from Compose tv
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(horizontal = 32.dp)
    ) {
        items(movies) { movie ->
            CustomCard(
                imageUrl = movie.imageUrl,
                onClick = { }
            )
        }
    }
}

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageUrl: String,
    cardAspectRatio: Float = 9f / 16f // Adjusting to 9:16 aspect ratio
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .width(160.dp)
            .height(250.dp)
            // .aspectRatio(cardAspectRatio) // Ensures the card follows 9:16 aspect ratio
            .background(Color.Transparent, RoundedCornerShape(16.dp))
            .padding(bottom = 8.dp),
    ) {
        AsyncImage(
            model = imageUrl, contentDescription = "Image",
            modifier = Modifier
                .fillMaxSize()
                .height(110.dp) // Adjust the height of image based on the aspect ratio
                .aspectRatio(cardAspectRatio), // Ensure the image maintains the aspect ratio
            contentScale = ContentScale.Crop, // Better for cropping and fitting
            alignment = Alignment.Center,
        )

    }
}

@Composable
fun MovieCard(movie: Movie) {
    var isFocused by remember { mutableStateOf(false) }
    // Focus handling
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(240.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .padding(4.dp)
    ) {
        AsyncImage(
            model = movie.imageUrl,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .then(if (isFocused) Modifier.background(Color.White.copy(alpha = 0.2f)) else Modifier)
            // optionally add scale on focus etc
        )
        // Title overlay, rating etc, as per design
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = movie.title,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            /* val rating = String.format("%.1f", movie.rating)
             val year = movie.year.take(4)*/
            Text(
                text = "⭐ 4.2 · 2025",
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}
