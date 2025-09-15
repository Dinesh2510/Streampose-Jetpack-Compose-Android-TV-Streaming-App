package com.pixeldev.composetv.screens.categories

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.width
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.Card
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.pixeldev.composetv.utlis.occupyScreenSize
import com.pixeldev.composetv.R
import com.pixeldev.composetv.data.remote.response.GenreResponse
import com.pixeldev.composetv.screens.common.CustomTopBar
import com.pixeldev.composetv.screens.home.JetStreamCardShape
import com.pixeldev.composetv.screens.home.HomeViewModel
import com.pixeldev.composetv.utlis.Constants.Companion.BASE_POSTER_IMAGE_URL
import com.pixeldev.composetv.utlis.MovieState
import com.pixeldev.composetv.utlis.TVGradientLoadingIndicator


@Composable
fun CategoryDetails(navHostController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    val genresMovieState by viewModel.genresMoviesResponses.collectAsState()
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        CustomTopBar(
            title = "Pick Your Mood \uD83C\uDF9E\uFE0F",
            onBackClick = { navHostController.popBackStack() },
            showBackButton = true
        )
        when (val state = genresMovieState) {
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
                PillIndicatorTabRow(state.data as GenreResponse?, viewModel)
            }
        }

    }

}

@Composable
private fun PillIndicatorTabRow(response: GenreResponse?, viewModel: HomeViewModel) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp, horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var isTabRowFocused by remember { mutableStateOf(false) }

        TabRow(
            modifier = Modifier.onFocusChanged {
                isTabRowFocused = it.isFocused || it.hasFocus
            },
            selectedTabIndex = selectedTabIndex, indicator = { tabPositions, _ ->
                if (selectedTabIndex >= 0) {
                    DashboardTopBarItemIndicator(
                        currentTabPosition = tabPositions[selectedTabIndex],
                        anyTabFocused = isTabRowFocused,
                        shape = JetStreamCardShape
                    )
                }
            },
            separator = { Spacer(modifier = Modifier) }) {
            response!!.genres.forEachIndexed { index, tab ->
                Tab(
                    modifier = Modifier
                        .height(32.dp),
                    selected = index == selectedTabIndex,
                    onFocus = {
                        selectedTabIndex = index
                        viewModel.setGenreData(tab.id!!.toInt())
                    },
                ) {
                    Text(
                        modifier = Modifier
                            .occupyScreenSize()
                            .padding(horizontal = 16.dp),
                        text = tab.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = LocalContentColor.current
                        )
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(2.dp)
                .background(MaterialTheme.colorScheme.border.copy(alpha = 0.6f))
        )
        TabPanels(selectedTabIndex = selectedTabIndex, viewModel)
    }
}

@Composable
private fun TabPanels(selectedTabIndex: Int, viewModel: HomeViewModel) {
    AnimatedContent(targetState = selectedTabIndex, label = "") {
        when (it) {
        }
        val genresWiseMoviePagination =
            viewModel.genresWiseMovieListState?.collectAsLazyPagingItems()

        Box(
            modifier = Modifier
                .fillMaxSize()
               /* .background(MaterialTheme.colorScheme.background)*/
        ) {
            val listState = rememberLazyGridState()

            if (genresWiseMoviePagination == null) {
                // Show loader jab tak list null hai
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    TVGradientLoadingIndicator()
                }
            } else {
                LazyVerticalGrid(
                    state = listState,
                    contentPadding = PaddingValues(top = 16.dp),
                    columns = GridCells.Adaptive(150.dp),
                ) {
                    items(genresWiseMoviePagination.itemCount) { i ->
                        val movie = genresWiseMoviePagination[i]
                        if (movie != null) {
                             PortraitMovieCard(
                                 title = movie.title ?: "",
                                 posterUrl = BASE_POSTER_IMAGE_URL + (movie.posterPath ?: "")
                             ) {
                                 // TODO: onClick handle
                             }
                        } else {
                            // Placeholder skeleton / shimmer item
                            Box(
                                modifier = Modifier
                                    .height(200.dp)
                                    .width(150.dp)
                                    .padding(8.dp)
                                    .background(Color.DarkGray.copy(alpha = 0.3f))
                            )
                        }
                    }

                    // Handle load states inside grid
                    genresWiseMoviePagination.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Box(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        TVGradientLoadingIndicator()
                                    }
                                }
                            }

                            loadState.append is LoadState.Loading -> {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Box(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        TVGradientLoadingIndicator()
                                    }
                                }
                            }

                            loadState.refresh is LoadState.Error -> {
                                val e = loadState.refresh as LoadState.Error
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    ErrorStrip(
                                        message = e.error.localizedMessage ?: "Unknown error"
                                    )
                                }
                            }

                            loadState.append is LoadState.Error -> {
                                val e = loadState.append as LoadState.Error
                                item(span = { GridItemSpan(maxLineSpan) }) {
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
    }
}

@Composable
fun PortraitMovieCard(title: String, posterUrl: String, content: @Composable () -> Unit) {
    BorderedFocusableItem(
        onClick = {

        },
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(0.6f),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            AsyncImage(
                model =  posterUrl,
                contentDescription = title,
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
                    text = title,
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
}

@Composable
fun DashboardTopBarItemIndicator(
    currentTabPosition: DpRect,
    modifier: Modifier = Modifier,
    activeColor: Color = Color(0xFFE5E1E6),
    inactiveColor: Color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
    anyTabFocused: Boolean,
    shape: Shape
) {
    val width by animateDpAsState(targetValue = currentTabPosition.width, label = "width")
    val height = currentTabPosition.height
    val leftOffset by animateDpAsState(targetValue = currentTabPosition.left, label = "leftOffset")
    val topOffset = currentTabPosition.top

    val pillColor by animateColorAsState(
        targetValue = if (anyTabFocused) activeColor else inactiveColor,
        label = "pillColor"
    )

    Box(
        modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart)
            .offset(x = leftOffset, y = topOffset)
            .width(width)
            .height(height)
            .background(color = pillColor, shape = shape)
            .zIndex(-1f)
    )
}

@Composable
fun ErrorStrip(message: String) {
    Card(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(8.dp)
        ) {
            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .width(42.dp)
                    .height(42.dp),
                painter = painterResource(id = R.drawable.logo_trans),
                contentDescription = "",
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text(
                color = Color.White,
                text = message,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp)
                    .align(CenterVertically)
            )
        }
    }
}