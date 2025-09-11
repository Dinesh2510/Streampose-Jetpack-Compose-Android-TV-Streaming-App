package com.pixeldev.composetv.screens.categories

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.pixeldev.composetv.R
import com.pixeldev.composetv.data.remote.response.GenreResponse
import com.pixeldev.composetv.graph.Screen
import com.pixeldev.composetv.screens.common.CustomTopBar
import com.pixeldev.composetv.screens.common.GradientBg
import com.pixeldev.composetv.screens.common.MovieCard
import com.pixeldev.composetv.screens.home.HomeViewModel
import com.pixeldev.composetv.utlis.MovieState
import com.pixeldev.composetv.utlis.TVGradientLoadingIndicator


@Composable
fun CategoryScreen(navHostController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    val genresMovieState by viewModel.genresMoviesResponses.collectAsState()
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
       /* Text(
            text = "Movie Categories",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontSize = 30.sp,
            modifier = Modifier
                .padding(vertical = 24.dp)
        )*/
        CustomTopBar(
            title = "Explore By Genres",
            onBackClick = { navHostController.popBackStack() },
            showBackButton = false
        )


        when (val state =genresMovieState) {
            is MovieState.Error -> {
                ErrorScreen(
                    title = "🛠️ Oops! The Hamsters Took a Coffee Break ☕",
                    subtitle = "Our servers are out chasing bugs... or maybe just napping.",
                    message = "We're having a little tech hiccup. Try again in a bit — or bribe the hamsters with snacks!",
                    imageRes = R.drawable.warning // replace with your drawable
                )
                /*ErrorScreen(
    title = "📡 No Connection Detected!",
    subtitle = "Even the satellites are ignoring us right now.",
    message = "Check your Wi-Fi or ethernet cable. Or go outside, touch grass. 🌱",
    imageRes = R.drawable.no_internet_image
)
*/
            }

            MovieState.Loading -> {
                TVGradientLoadingIndicator()
            }

            is MovieState.Success<*> -> {
                setGenresData(state.data as GenreResponse?,navHostController)
            }
        }

    }

}

@Composable
fun setGenresData(genreResponse: GenreResponse?, navHostController: NavHostController,) {
    var genreData = genreResponse!!.genres
    val gridColumns: Int = 4
    val lazyGridState = rememberLazyGridState()
    val shouldShowTopBar by remember {
        derivedStateOf {
            lazyGridState.firstVisibleItemIndex == 0 &&
                    lazyGridState.firstVisibleItemScrollOffset < 100
        }
    }
    LaunchedEffect(shouldShowTopBar) {
        // onScroll(shouldShowTopBar)
        Log.d("TAG", "CategoryScreen: shouldShowTopBar")
    }
    LazyVerticalGrid(
        state = lazyGridState,
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(gridColumns),
    ) {


        items(genreData.size) { index ->
            var isFocused by remember { mutableStateOf(false) }
            MovieCard(
                onClick = {
                    navHostController.navigate(Screen.CategoryDetailsScreen.route)
                },
                modifier = Modifier
                    .padding(8.dp)
                    .aspectRatio(16 / 9f)
                /*.onFocusChanged {
                    isFocused = it.isFocused || it.hasFocus
                }
                .focusProperties {
                    if (index % gridColumns == 0) {
                        left = FocusRequester.Cancel
                    }
                }*/
            ) {
                val itemAlpha by animateFloatAsState(
                    targetValue = if (isFocused) .6f else 0.2f,
                    label = ""
                )
                val textColor = if (isFocused) Color.White else Color.White

                Box(contentAlignment = Alignment.Center) {
                    GradientBg()
                    Text(
                        text = genreData[index].name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}
