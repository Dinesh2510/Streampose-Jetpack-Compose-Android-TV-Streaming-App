package com.pixeldev.composetv.screens.common

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ClassicCard
import androidx.tv.material3.CompactCard
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.StandardCardContainer
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import androidx.tv.material3.WideCardContainer
import androidx.tv.material3.WideClassicCard
import com.pixeldev.composetv.R

class CardLayout {

}

@Composable
fun WideClassicCardUI() {
    WideClassicCard(
        onClick = { },
        modifier = Modifier,
        image = {
            Image(
                modifier = Modifier
                    .width(180.dp)
                    .aspectRatio(CardDefaults.HorizontalImageAspectRatio),
                painter = painterResource(id = R.drawable.card_thumbnail),
                contentDescription = null,
            )
        },
        title = {
            Text(
                text = "Title goes here",
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
        description = {
            Text(
                text = "A cruel tryst of fate deserts a loyal dog from his master infin...",
                modifier = Modifier
                    .width(200.dp)
                    .padding(
                        top = 4.dp,
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 8.dp
                    )
            )
        }
    )
}

@Composable
fun WideCardContainerUI() {
    WideCardContainer(
        modifier = Modifier,
        imageCard = {
            Card(
                onClick = { },
                interactionSource = it,
                colors = CardDefaults.colors(containerColor = Color.Transparent)
            ) {
                Image(
                    modifier = Modifier
                        .width(180.dp)
                        .aspectRatio(CardDefaults.HorizontalImageAspectRatio),
                    painter = painterResource(id = R.drawable.card_thumbnail),
                    contentDescription = null,
                )
            }
        },
        title = {
            Text(
                text = "Title goes here",
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
        },
        subtitle = {
            Text(
                text = "Secondary · text",
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        },
    )
}

@Composable
fun ClassicCardUI() {
    ClassicCard(
        onClick = { },
        modifier = Modifier.width(180.dp),
        image = {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(CardDefaults.HorizontalImageAspectRatio),
                painter = painterResource(id = R.drawable.card_thumbnail),
                contentDescription = null,
            )
        },
        title = {
            Text(
                text = "Title goes here",
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
}

@Composable
fun StandardCardContainerUI() {
    StandardCardContainer(
        modifier = Modifier.width(180.dp),
        imageCard = {
            Card(
                onClick = { },
                interactionSource = it,
                colors = CardDefaults.colors(containerColor = Color.Transparent)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(CardDefaults.HorizontalImageAspectRatio),
                    painter = painterResource(id = R.drawable.card_thumbnail),
                    contentDescription = null,
                )
            }
        },
        title = {
            Text(
                text = "Title goes here",
                modifier = Modifier.padding(top = 8.dp)
            )
        },
        subtitle = {
            Text(text = "Secondary · text")
        },
    )
}

@Composable
fun CompactCardUi() {
    CompactCard(
        onClick = { },
        modifier = Modifier
            .width(180.dp),
        image = {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(CardDefaults.HorizontalImageAspectRatio),
                painter = painterResource(id = R.drawable.card_thumbnail),
                contentDescription = null,
            )
        },
        title = {
            Text(
                text = "Title goes here",
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
}

private val tabs = listOf("Search", "Home", "Movies", "Shows", "Library", "Settings")

private fun getTabIndex(text: String): Int {
    return tabs.indexOf(text)
}

@Composable
fun TabIndicatorTabRow() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp, horizontal = 32.dp).onFocusChanged {
                Log.d("FocusDebug", "Tab $selectedTabIndex isFocused: ${it.isFocused}")
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = index == selectedTabIndex,
                    onFocus = { selectedTabIndex = index },
                ) {
                    Text(
                        text = tab,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }

        TabPanels(selectedTabIndex = selectedTabIndex)
    }
}

@Composable
private fun TabPanels(selectedTabIndex: Int) {
    AnimatedContent(targetState = selectedTabIndex, label = "") {
        when (it) {
            getTabIndex("Search") -> {

            }

            getTabIndex("Home") -> {
            }

            getTabIndex("Movies") -> {}
            getTabIndex(
                "Shows"
            ) -> {
            }

            getTabIndex("Library") -> {}
            getTabIndex("Settings") -> {
            }
        }
    }
}
