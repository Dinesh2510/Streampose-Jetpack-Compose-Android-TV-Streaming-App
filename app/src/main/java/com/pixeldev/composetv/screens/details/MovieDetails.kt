/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pixeldev.composetv.screens.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ShapeDefaults
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pixeldev.composetv.utlis.TitleValueText
import com.pixeldev.composetv.utlis.rememberChildPadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieDetails(
    goToMoviePlayer: () -> Unit
) {
    val childPadding = rememberChildPadding()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(432.dp)
            .bringIntoViewRequester(bringIntoViewRequester)
    ) {
        MovieImageWithGradients(
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxWidth(0.55f)) {
            Spacer(modifier = Modifier.height(108.dp))
            Column(
                modifier = Modifier.padding(start = childPadding.start)
            ) {
                MovieLargeTitle(movieTitle = "movieDetails.name")

                Column(
                    modifier = Modifier.alpha(0.75f)
                ) {
                    MovieDescription(description = "description")
                    DotSeparatedRow(
                        modifier = Modifier.padding(top = 20.dp),
                        texts = listOf(
                            "pgRating",
                            "releaseDate",
                           " categories.joinToString(", ")",
                            "duration"
                        )
                    )
                    DirectorScreenplayMusicRow(
                        director = "director",
                        screenplay = "screenplay",
                        music = "music"
                    )
                }
                WatchTrailerButton(
                    modifier = Modifier.onFocusChanged {
                        if (it.isFocused) {
                            coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
                        }
                    },
                    goToMoviePlayer = goToMoviePlayer
                )
            }
        }
    }
}

@Composable
private fun WatchTrailerButton(
    modifier: Modifier = Modifier,
    goToMoviePlayer: () -> Unit
) {
    Button(
        onClick = goToMoviePlayer,
        modifier = modifier.padding(top = 24.dp),
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        shape = ButtonDefaults.shape(shape = ShapeDefaults.ExtraSmall)
    ) {
        Icon(
            imageVector = Icons.Outlined.PlayArrow,
            contentDescription = null
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = "watch_trailer",
            style = MaterialTheme.typography.titleSmall

        )
    }
}

@Composable
private fun DirectorScreenplayMusicRow(
    director: String,
    screenplay: String,
    music: String
) {
    Row(modifier = Modifier.padding(top = 32.dp)) {
        TitleValueText(
            modifier = Modifier
                .padding(end = 32.dp)
                .weight(1f),
            title = "abcd",
            value = director
        )

        TitleValueText(
            modifier = Modifier
                .padding(end = 32.dp)
                .weight(1f),
            title = "abcd",
            value = screenplay
        )

        TitleValueText(
            modifier = Modifier.weight(1f),
            title = "abcd",
            value = music
        )
    }
}

@Composable
private fun MovieDescription(description: String) {
    Text(
        text = description,
        style = MaterialTheme.typography.titleSmall.copy(
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal
        ),
        modifier = Modifier.padding(top = 8.dp),
        maxLines = 2
    )
}

@Composable
private fun MovieLargeTitle(movieTitle: String) {
    Text(
        text = movieTitle,
        style = MaterialTheme.typography.displayMedium.copy(
            fontWeight = FontWeight.Bold
        ),
        color = Color.White,
        maxLines = 1
    )
}

@Composable
private fun MovieImageWithGradients(
    modifier: Modifier = Modifier,
    gradientColor: Color = MaterialTheme.colorScheme.background,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://picsum.photos/id/111/300/180")
            .crossfade(true)
            .build(),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = modifier.drawWithContent {
            drawContent() // draw image first ✅
            // Left side gradient fade
            drawRect(
                Brush.horizontalGradient(
                    colors = listOf(gradientColor.copy(alpha = 0.95f), Color.Transparent),
                    startX = 0f,          // start from left edge
                    endX = size.width / 2f  // fade to middle
                )
            )

            // Vertical bottom fade
            drawRect(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, gradientColor.copy(alpha = 0.9f)), // ✅ alpha
                    startY = size.height / 2f,
                    endY = size.height
                )
            )

            // Horizontal right fade
            drawRect(
                Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, gradientColor.copy(alpha = 0.9f)), // ✅ alpha
                    startX = size.width ,
                    endX = size.width/ 2f
                )
            )

            // Diagonal top-right fade
            drawRect(
                Brush.linearGradient(
                    colors = listOf(Color.Transparent, gradientColor.copy(alpha = 0.8f)), // ✅ alpha
                    start = Offset(x = size.width / 2f, y = size.height / 2f),
                    end = Offset(x = size.width, y = 0f)
                )
            )
        }
    )
}

