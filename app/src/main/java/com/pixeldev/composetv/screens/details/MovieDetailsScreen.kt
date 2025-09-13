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

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.pixeldev.composetv.utlis.TitleValueText
import com.pixeldev.composetv.utlis.rememberChildPadding
import com.pixeldev.composetv.R
import com.pixeldev.composetv.screens.common.StandardCardContainerUI

object MovieDetailsScreen {
    const val MovieIdBundleKey = "movieId"
}

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

@Composable
fun ScaleAbleAvatar(
    modifier: Modifier,
    avatarRes: Int,
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
fun AvatarIcon(modifier: Modifier, @DrawableRes avatarRes: Int, description: String? = null) {
    Image(
        painter = painterResource(id = avatarRes),
        contentDescription = description,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(CircleShape),
    )
}
