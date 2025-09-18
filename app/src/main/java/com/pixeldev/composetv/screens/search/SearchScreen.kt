package com.pixeldev.composetv.screens.search

import android.view.KeyEvent
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.pixeldev.composetv.R
import com.pixeldev.composetv.models.Search
import com.pixeldev.composetv.screens.favourite.Movie
import com.pixeldev.composetv.screens.favourite.NetflixPosterCard
import com.pixeldev.composetv.screens.home.JetStreamCardShape
import com.pixeldev.composetv.utlis.CommonImage
import com.pixeldev.composetv.utlis.Constants.Companion.BASE_POSTER_IMAGE_URL
import com.pixeldev.composetv.utlis.TVGradientLoadingIndicator

@Composable
fun SearchScreen(
    onMovieClick: (movie: Search) -> Unit,
    searchScreenViewModel: SearchScreenViewModel = hiltViewModel(),
) {

    val moviesFlow = searchScreenViewModel.multiSearchState.value
    val lazyPagingItems = moviesFlow.collectAsLazyPagingItems()

    SearchResult(
        lazyPagingItems = lazyPagingItems,
        searchMovies = searchScreenViewModel::query,
        onMovieClick = onMovieClick,
        modifier = Modifier.fillMaxSize()
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun SearchResult(
    lazyPagingItems: LazyPagingItems<Search>,
    searchMovies: (queryString: String) -> Unit,
    onMovieClick: (movie: Search) -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchQuery by remember { mutableStateOf("") }
    val tfFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val tfInteractionSource = remember { MutableInteractionSource() }
    val isTfFocused by tfInteractionSource.collectIsFocusedAsState()
    var submittedQuery by remember { mutableStateOf("") } // ✅ Track submitted query
    var context = LocalContext.current
    LaunchedEffect(Unit) { tfFocusRequester.requestFocus() }
    Column(modifier = modifier.fillMaxSize()) {
        // 🔍 Search Box
        Surface(
            shape = ClickableSurfaceDefaults.shape(shape = JetStreamCardShape),
            scale = ClickableSurfaceDefaults.scale(focusedScale = 1f),
            colors = ClickableSurfaceDefaults.colors(
                containerColor = if (isTfFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                else MaterialTheme.colorScheme.inverseOnSurface,
                focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                pressedContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
                focusedContentColor = MaterialTheme.colorScheme.onSurface,
                pressedContentColor = MaterialTheme.colorScheme.onSurface
            ),
            tonalElevation = 4.dp,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .border(
                    width = if (isTfFocused) 2.dp else 1.dp,
                    color = if (isTfFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.border,
                    shape = JetStreamCardShape
                )
                .focusable(true)
                .onKeyEvent() {
                    if (it.nativeKeyEvent.action == KeyEvent.ACTION_UP) {
                        when (it.nativeKeyEvent.keyCode) {
                            KeyEvent.KEYCODE_DPAD_UP -> {
                                // focusManager.moveFocus(FocusDirection.Up)
                                tfFocusRequester.requestFocus()
                                Toast.makeText(context, "Up...", Toast.LENGTH_SHORT).show()
                            }

                            KeyEvent.KEYCODE_DPAD_DOWN -> focusManager.moveFocus(FocusDirection.Down)
                            KeyEvent.KEYCODE_BACK -> focusManager.clearFocus()
                        }
                    }
                    true

                },
            onClick = { tfFocusRequester.requestFocus() }
        ) {
            BasicTextField(
                value = searchQuery,
                onValueChange = { updatedQuery -> searchQuery = updatedQuery },
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 20.dp),
                    ) {
                        innerTextField()
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = stringResource(R.string.search_hint),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(tfFocusRequester)
                    .onKeyEvent {
                        if (it.nativeKeyEvent.action == KeyEvent.ACTION_UP) {
                            when (it.nativeKeyEvent.keyCode) {
                                KeyEvent.KEYCODE_DPAD_UP -> {
                                    /* focusManager.moveFocus(FocusDirection.Up)
                                     tfFocusRequester.requestFocus()*/
                                    Toast.makeText(context, "up text...", Toast.LENGTH_SHORT).show()
                                }

                                KeyEvent.KEYCODE_DPAD_DOWN -> focusManager.moveFocus(FocusDirection.Down)
                                KeyEvent.KEYCODE_BACK -> focusManager.clearFocus()
                                KeyEvent.KEYCODE_DPAD_CENTER, // ✅ Remote OK/Enter
                                KeyEvent.KEYCODE_ENTER -> {
                                    submittedQuery = searchQuery
                                    searchMovies(searchQuery)
                                    return@onKeyEvent true
                                }
                            }
                        }
                        true
                    },
                interactionSource = tfInteractionSource,
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { searchMovies(searchQuery) }
                ),
                maxLines = 1,
                textStyle = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🎬 Movies Grid or Empty States
        when {
            lazyPagingItems.loadState.refresh is LoadState.Loading && searchQuery.isNotBlank() -> {
                // Only show loading when user searched
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    TVGradientLoadingIndicator()
                    Text("Loading...", color = Color.White)
                }
            }

            lazyPagingItems.itemCount == 0 && searchQuery.isNotBlank() -> {
                // No results found after a search
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No results found", color = Color.White)
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 140.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(lazyPagingItems.itemCount) { index ->
                        lazyPagingItems[index]?.let { movie ->
                            PosterCard(movie = movie) {
                                onMovieClick(movie)
                            }
                        }
                    }

                    // Append state
                    if (lazyPagingItems.loadState.append is LoadState.Loading) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Text("Loading more...", Modifier.padding(16.dp), color = Color.White)
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun PosterCard(
    movie: Search,
    onClick: () -> Unit,
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
            .height(250.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
        /*.onFocusChanged { focusState ->
            isFocused = focusState.isFocused
            if (focusState.isFocused) {
                onFocus()
            }
        }*/,
        colors = CardDefaults.colors(
            containerColor = Color.Transparent
        ),
        border = CardDefaults.border(
            focusedBorder = Border(
                BorderStroke(3.dp, Color.White)
            )
        )
    ) {
        CommonImage(
            imageUrl = BASE_POSTER_IMAGE_URL + movie.posterPath,
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

    }
}
