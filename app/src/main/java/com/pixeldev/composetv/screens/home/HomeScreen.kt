package com.pixeldev.composetv.screens.home


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import androidx.tv.material3.*
import com.pixeldev.composetv.data.remote.response.GenreResponse
import com.pixeldev.composetv.data.remote.response.MovieResponse
import com.pixeldev.composetv.graph.Screen
import com.pixeldev.composetv.screens.details.Top10MoviesListPreview
import com.pixeldev.composetv.utlis.MovieState
import com.pixeldev.composetv.utlis.TVGradientLoadingIndicator

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), navController: NavHostController) {


    ShowHomeScreenData(viewModel, navController)


}

@Composable
fun SampleImmersiveList() {
    val items = remember { listOf(Color.Red, Color.Green, Color.Yellow) }
    val selectedItem = remember { mutableStateOf<Color?>(null) }

    // Container
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(400.dp)) {
        val bgColor = selectedItem.value

        // Background
        if (bgColor != null) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(20f / 7)
                .background(bgColor)) {}
        }

        // Rows
        LazyRow(
            modifier = Modifier.align(Alignment.BottomEnd),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(20.dp),
        ) {
            items(items) { color ->
                Surface(
                    onClick = {},
                    modifier =
                        Modifier
                            .width(200.dp)
                            .aspectRatio(16f / 9)
                            .onFocusChanged {
                                if (it.hasFocus) {
                                    selectedItem.value = color
                                }
                            },
                    colors =
                        ClickableSurfaceDefaults.colors(
                            containerColor = color,
                            focusedContainerColor = color,
                        ),
                    border =
                        ClickableSurfaceDefaults.border(
                            focusedBorder =
                                Border(border = BorderStroke(2.dp, Color.White), inset = 4.dp)
                        ),
                ) {}
            }
        }
    }
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
                ErrorContent(
                    listOf(
                        discoveryMovieState,
                        trendingMovieState,
                        nowPlayingMovieState,
                        upcomingMovieState,
                        genresMovieState
                    )
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
            .background(MaterialTheme.colorScheme.surface),
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
                    .height(324.dp)
            )
        }
        item {
            /*WideClassicCardUI()
            WideCardContainerUI()
            ClassicCardUI()
            StandardCardContainerUI()
            CompactCardUi()*/
            //  SampleImmersiveList()
            Top10MoviesListPreview(trendingMovie)
        }

        val categories = (1..10).map { "Category $it" }

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
                    ) {
                        navController.navigate(Screen.MovieDetails.route)
                    }
                }
            }

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
