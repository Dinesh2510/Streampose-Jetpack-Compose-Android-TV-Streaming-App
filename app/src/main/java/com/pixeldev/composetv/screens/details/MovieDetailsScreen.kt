package com.pixeldev.composetv.screens.details

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.pixeldev.composetv.utlis.rememberChildPadding
import com.pixeldev.composetv.data.remote.response.MovieDetailsDTO
import com.pixeldev.composetv.data.remote.response.MovieResponse
import com.pixeldev.composetv.models.Cast
import com.pixeldev.composetv.models.Movies
import com.pixeldev.composetv.screens.categories.ErrorStrip
import com.pixeldev.composetv.utlis.Constants.Companion.BASE_POSTER_IMAGE_URL
import com.pixeldev.composetv.utlis.MovieState
import com.pixeldev.composetv.utlis.TVGradientLoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.toUpperCase
import androidx.tv.material3.Border
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ClassicCard
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Surface
import coil.compose.rememberAsyncImagePainter
import com.pixeldev.composetv.utlis.SectionHeader
import com.pixeldev.composetv.utlis.TitleValueText
import com.pixeldev.composetv.utlis.formatToMillions
import java.util.Locale

@Composable
fun MovieDetailsScreen(
    movieId: String,
    onBackPressed: () -> Unit,
    viewModel: MovieDetailsViewModel = hiltViewModel(),
) {
    // Collect UI state
    val movieDetailsState by viewModel.detailsMovieResponses.collectAsState()
    val similarMoviesState by viewModel.similarMovieResponses.collectAsState()
    val castState by viewModel.castMovieResponses.collectAsState()

    // Trigger API calls once
    LaunchedEffect(movieId) {
        viewModel.fetchMoviesDetails(movieId)
        viewModel.fetchSimilarMovies(movieId)
        viewModel.fetchCasteOfMovies(movieId)
    }

    SetDetails(
        goToMoviePlayer = { },
        onBackPressed = onBackPressed,
        movieDetailsState = movieDetailsState,
        similarMoviesState = similarMoviesState,
        castState = castState,
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
    )
}

@Composable
private fun SetDetails(
    goToMoviePlayer: () -> Unit,
    onBackPressed: () -> Unit,
    movieDetailsState: MovieState<MovieDetailsDTO?>,
    similarMoviesState: MovieState<MovieResponse?>,
    castState: MovieState<List<Cast>?>,
    modifier: Modifier = Modifier,
) {
    val childPadding = rememberChildPadding()
    BackHandler(onBack = onBackPressed)
    val movieDetails = (movieDetailsState as? MovieState.Success)?.data
    val castList = (castState as? MovieState.Success)?.data ?: emptyList()
    val similarMovies = (similarMoviesState as? MovieState.Success)?.data?.results ?: emptyList()


    LazyColumn(
        contentPadding = PaddingValues(bottom = 135.dp),
        modifier = modifier,
    ) { // Movie Details
        item {
            handleState(movieDetailsState) {
                movieDetails?.let {
                    MovieDetails(
                        goToMoviePlayer = goToMoviePlayer,
                        movieDetails = it
                    )
                }
            }
        }

        // Cast Section
        item {
            handleState(castState) {
                CastSection(castList = castList)
            }
        }

        // Related / Similar Movies
        item {
            handleState(similarMoviesState) {
                RelatedMoviesSection(movies = similarMovies)
            }
        }

        // Reviews (reuses movieDetails)
        item {
            if (movieDetailsState is MovieState.Loading) {
                Box(Modifier.fillMaxWidth()) {
                    TVGradientLoadingIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else if (movieDetailsState is MovieState.Error) {
                ErrorStrip(movieDetailsState.message)
            } else {
                movieDetails?.let {
                    MovieReviews(
                        movie = it,
                        modifier = Modifier.padding(top = childPadding.top)
                    )
                }
            }
        }

        // Divider
        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = childPadding.start)
                    .padding(PaddingValues(vertical = 48.dp))
                    .fillMaxWidth()
                    .height(1.dp)
                    .alpha(0.15f)
                    .background(MaterialTheme.colorScheme.onSurface)
            )
        }

        // Basic Movie Data Section
        item {
            movieDetails?.let {
                SetMovieBasicData(it)
            }
        }
    }
}

@Composable
fun SetMovieBasicData(movies: MovieDetailsDTO) {
    val childPadding = rememberChildPadding()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = childPadding.start),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val itemModifier = Modifier.width(192.dp)

        TitleValueText(
            modifier = itemModifier,
            title = "Status",
            value = movies.status
        )
        TitleValueText(
            modifier = itemModifier,
            title = "Original Language",
            value = movies.originalLanguage.uppercase(Locale.ROOT)
        )
        TitleValueText(
            modifier = itemModifier,
            title = "Budget",
            value = formatToMillions(movies.budget).toString()
        )
        TitleValueText(
            modifier = itemModifier,
            title = "Revenue",
            value = formatToMillions(movies.revenue).toString()
        )
    }
}


@Composable
fun CastSection(castList: List<Cast>) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SectionHeader("Cast")
        Spacer(Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
        ) {
            items(castList.size) { cast ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ScaleAbleAvatar(
                        avatarRes = BASE_POSTER_IMAGE_URL + castList[cast].profilePath,
                        modifier = Modifier.padding(8.dp),
                        onProfileSelection = { }
                    )
                    Text(
                        castList[cast].name,
                        color = Color.White,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun RelatedMoviesSection(movies: List<Movies?>) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SectionHeader("Related Movies")
        Spacer(Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
        ) {
            items(movies.size) { movie ->
                ClassicMovieCard(
                    title = movies[movie]!!.title ?: "",
                    imageUrl = BASE_POSTER_IMAGE_URL + (movies[movie]!!.posterPath ?: ""),
                    year = movies[movie]!!.releaseDate.toString(),
                    rating = movies[movie]!!.voteAverage,
                    onClick = {},
                )
            }
        }
    }
}

@Composable
fun ClassicMovieCard(
    title: String,
    imageUrl: String,
    year: String?,
    rating: Double?,
    onClick: () -> Unit
) {
    ClassicCard(
        onClick = { onClick() },
        modifier = Modifier
            .width(160.dp)
            .height(290.dp),
        image = {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .aspectRatio(CardDefaults.VerticalImageAspectRatio),
            )
        },
        title = {
            Text(
                text = title,
                maxLines = 1,
                modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
            )
        },
        subtitle = {
            val rating = rating?.let { String.format("%.1f", it) } ?: "-"
            val year = year?.take(4) ?: "-"
            Text(
                text =
                    "⭐ $rating · $year",
                modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
            )
        },
    )
}


@Composable
fun ScaleAbleAvatar(
    modifier: Modifier,
    avatarRes: String,
    onProfileSelection: () -> Unit
) {
    Surface(
        onClick = {
            onProfileSelection()
        },
        modifier = modifier.size(110.dp), // Enforce a square area
        border = ClickableSurfaceDefaults.border(
            border = Border(
                border = BorderStroke(
                    1.dp,
                    Color.Transparent,
                ),
                shape = CircleShape,
            ),
            focusedBorder = Border(
                border = BorderStroke(width = 2.dp, color = Color.White),
                inset = 0.dp,
            ),

            ),
        shape = ClickableSurfaceDefaults.shape(shape = CircleShape), // Corrected shape
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1.2f),
    ) {
        AvatarIcon(avatarRes = avatarRes, modifier = Modifier.fillMaxSize())
    }
}


@Composable
fun AvatarIcon(modifier: Modifier, avatarRes: String, description: String? = null) {
    Image(
        painter = rememberAsyncImagePainter(
            model = avatarRes
        ),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(CircleShape),
    )
}
// Centralized error/loading handling (optional for cleaner layout)
@Composable
fun <T> handleState(
    state: MovieState<T>,
    onSuccess: @Composable (T) -> Unit
) {
    when (state) {
        is MovieState.Loading -> Box(Modifier.fillMaxWidth()) {
            TVGradientLoadingIndicator(modifier = Modifier.align(Alignment.Center))
        }
        is MovieState.Error -> ErrorStrip(state.message)
        is MovieState.Success -> {
            state.data?.let { onSuccess(it) }
        }
    }
}


/*


@Composable
fun MovieDetailsScreen(
    goToMoviePlayer: () -> Unit,
    onBackPressed: () -> Unit,
) {


    Details(
        goToMoviePlayer = goToMoviePlayer,
        onBackPressed = onBackPressed,
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
    )
}

@Composable
private fun Details(
    goToMoviePlayer: () -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val childPadding = rememberChildPadding()

    BackHandler(onBack = onBackPressed)
    LazyColumn(
        contentPadding = PaddingValues(bottom = 135.dp),
        modifier = modifier,
    ) {
        item {
            MovieDetails(
                goToMoviePlayer = goToMoviePlayer
            )
        }

        item {
            CastSection()
        }

        item {
            RelatedMoviesSection()
        }

        item {
            MovieReviews(
                modifier = Modifier.padding(top = childPadding.top),
            )
        }

        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = childPadding.start)
                    .padding(BottomDividerPadding)
                    .fillMaxWidth()
                    .height(1.dp)
                    .alpha(0.15f)
                    .background(MaterialTheme.colorScheme.onSurface)
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = childPadding.start),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val itemModifier = Modifier.width(192.dp)

                TitleValueText(
                    modifier = itemModifier,
                    title = "kajsbd",
                    value = "status"
                )
                TitleValueText(
                    modifier = itemModifier,
                    title = "ksmd",
                    value = "originalLanguage"
                )
                TitleValueText(
                    modifier = itemModifier,
                    title = ";lskjdf",
                    value = "budget"
                )
                TitleValueText(
                    modifier = itemModifier,
                    title = "lkasf",
                    value = "revenue"
                )
            }
        }
    }
}

private val BottomDividerPadding = PaddingValues(vertical = 48.dp)


@Composable
fun RelatedMoviesSection() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Related Movies", style = MaterialTheme.typography.headlineSmall, color = Color.White)
        Spacer(Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(10) {index->
                StandardCardContainerUI()
            }
        }
    }
}

@Composable
fun CastSection() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Cast", style = MaterialTheme.typography.headlineSmall, color = Color.White)
        Spacer(Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp
            ) // Add padding to avoid overflow
        ) {
            items(10) { index ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ScaleAbleAvatar(
                        avatarRes = R.drawable.ic_logo,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                        onProfileSelection = { }
                    )
                    Text(
                        "Actor $index",
                        color = Color.White,
                        modifier = Modifier.padding(top = 8.dp) // Ensure space between the avatar and text
                    )
                }
            }
        }
    }
}

*/
