package com.pixeldev.composetv.screens.home


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ModalNavigationDrawer
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Text
import androidx.tv.material3.rememberDrawerState
import androidx.tv.material3.*
import coil.compose.rememberAsyncImagePainter
import com.pixeldev.composetv.R
import com.pixeldev.composetv.screens.ClassicCardUI
import com.pixeldev.composetv.screens.CompactCardUi
import com.pixeldev.composetv.screens.PillIndicatorTabRow
import com.pixeldev.composetv.screens.StandardCardContainerUI
import com.pixeldev.composetv.screens.WideCardContainerUI
import com.pixeldev.composetv.screens.WideClassicCardUI
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), navController: NavHostController) {

    val discoveryMovieState by viewModel.discoveryMovieResponses.collectAsState()
    val trendingMovieState by viewModel.trendingMovieResponses.collectAsState()
    val nowPlayingMovieState by viewModel.nowPlayingMoviesResponses.collectAsState()
    val upcomingMovieState by viewModel.upcomingMoviesResponses.collectAsState()
    val genresMovieState by viewModel.genresMoviesResponses.collectAsState()
    val moviesLazyPagingItems = viewModel.popularAllListState.collectAsLazyPagingItems()
    val categories = (1..10).map { "Category $it" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        item(contentType = "FeaturedMoviesCarousel") {
            PillIndicatorTabRow()

            FeaturedMoviesCarousel(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(324.dp)
            )
        }
        item {/*
            WideClassicCardUI()
            WideCardContainerUI()
            ClassicCardUI()
            StandardCardContainerUI()
            CompactCardUi()*/
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
                    )
                }
            }

        }

    }

}
