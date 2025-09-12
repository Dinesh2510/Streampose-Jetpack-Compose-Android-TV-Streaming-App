package com.pixeldev.composetv.screens.home


import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import androidx.tv.material3.*
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.jetstream.presentation.utils.SectionHeader
import com.pixeldev.composetv.R
import com.pixeldev.composetv.data.remote.response.GenreResponse
import com.pixeldev.composetv.data.remote.response.MovieResponse
import com.pixeldev.composetv.graph.Screen
import com.pixeldev.composetv.models.Genre
import com.pixeldev.composetv.models.Movies
import com.pixeldev.composetv.screens.categories.ErrorScreen
import com.pixeldev.composetv.screens.details.Top10MoviesListPreview
import com.pixeldev.composetv.utlis.Constants.Companion.BASE_BACKDROP_IMAGE_URL_300
import com.pixeldev.composetv.utlis.MovieState
import com.pixeldev.composetv.utlis.TVGradientLoadingIndicator

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), navController: NavHostController) {
    ShowHomeScreenData(viewModel, navController)
}

@Composable
fun ShowHomeScreenData(viewModel: HomeViewModel = viewModel(), navController: NavHostController) {
    val discoveryMovieState by viewModel.discoveryMovieResponses.collectAsState()
    val trendingMovieState by viewModel.trendingMovieResponses.collectAsState()
    val nowPlayingMovieState by viewModel.nowPlayingMoviesResponses.collectAsState()
    val upcomingMovieState by viewModel.upcomingMoviesResponses.collectAsState()
    val genresMovieState by viewModel.genresMoviesResponses.collectAsState()
    val moviesLazyPagingItems = viewModel.popularAllListState.collectAsLazyPagingItems()
    val categories = (1..10).map { "Category $it" }

    /*  Log.d("TAG_ShowHome", "ShowHomeScreenData: "+discoveryMovieState)
      when (val state = discoveryMovieState) {
          is MovieState.Loading -> {
              TVGradientLoadingIndicator()
          }
          is MovieState.Success -> {FeaturedMoviesCarousel(
              state.data,
              modifier = Modifier
                  .fillMaxWidth()
                  .height(324.dp)
          )}
          is MovieState.Error -> Text("Error: ${state.message}", color = Red)
      }*/


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
                /* ErrorContent(
                     listOf(
                         discoveryMovieState,
                         trendingMovieState,
                         nowPlayingMovieState,
                         upcomingMovieState,
                         genresMovieState
                     )
                 )*/
                ErrorScreen(
                    title = "🛠️ Oops! The Hamsters Took a Coffee Break ☕",
                    subtitle = "Our servers are out chasing bugs... or maybe just napping.",
                    message = "We're having a little tech hiccup. Try again in a bit — or bribe the hamsters with snacks!",
                    imageRes = R.drawable.warning // replace with your drawable
                )
            }

            allSuccess -> {
                MovieContent(
                    discoveryMovie = (discoveryMovieState as MovieState.Success).data,
                    trendingMovie = (trendingMovieState as MovieState.Success).data,
                    nowPlayingMovie = (nowPlayingMovieState as MovieState.Success).data,
                    upcomingMovie = (upcomingMovieState as MovieState.Success).data,
                    genresMovie = (genresMovieState as MovieState.Success).data,
                    navController
                )
            }
        }
    }
}

@Composable
fun MovieContent(
    discoveryMovie: MovieResponse?,
    trendingMovie: MovieResponse?,
    nowPlayingMovie: MovieResponse?,
    upcomingMovie: MovieResponse?,
    genresMovie: GenreResponse?,
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        item() {
            HomeTopBar()
        }
        item() {

            FeaturedMoviesCarousel(
                discoveryMovie,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(324.dp)
            )
        }
        item {
            Top10MoviesListPreview(trendingMovie)
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
            MovieSection(
                title = "Upcoming Movies",
                movies = upcomingMovie?.results,
                onClickMovieCard = { navController.navigate(Screen.MovieDetails.route) }
            )
        }

        item {
            GenreSection(genres = genresMovie?.genres)
        }

    }
}

@Composable
fun MovieSection(title: String, movies: ArrayList<Movies>?, onClickMovieCard: () -> Unit) {
    Column {
        SectionHeader(title)

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp), // Or remove this line
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp), // Control padding here
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(movies ?: emptyList()) { movie ->
                TvCardItem(
                    title = "${movie.title}",
                    imageUrl = BASE_BACKDROP_IMAGE_URL_300 + movie.backdropPath
                ) { onClickMovieCard() }
            }
        }
    }
}



@Composable
fun TvCardItem(
    title: String,
    imageUrl: String,
    onClickPressed: () -> Unit,
) {
    CompactCard(
        onClick = { onClickPressed() },
        modifier = Modifier
            .width(200.dp),
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

@Composable
fun GenreSection(genres: List<Genre>?) {
    Column {
        SectionHeader("Genres")

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 8.dp), // 👈 Gives breathing room at edges
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){
            items(genres ?: emptyList()) { genre ->
                CategoryItem(
                    genre = genre,
                    modifier = Modifier,
                    onClick = { }
                )
            }
        }
    }
}



@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun CategoryItem(
    modifier: Modifier = Modifier,
    genre: Genre,
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
                    text = genre.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun GenreCard(genre: Genre) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(100.dp),
        onClick = {}
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = genre.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
@Composable
fun ErrorContent(states: List<MovieState<*>>) {
    Column(modifier = Modifier.padding(32.dp)) {
        Text("Something went wrong:", color = Color.Red)
        states.forEachIndexed { index, state ->
            if (state is MovieState.Error) {
                Text("API ${index + 1} failed: ${state.message}", color = Color.Red)
            }
        }
    }
}
