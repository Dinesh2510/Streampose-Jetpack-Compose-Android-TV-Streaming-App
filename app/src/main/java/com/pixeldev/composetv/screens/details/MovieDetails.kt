package com.pixeldev.composetv.screens.details

import android.widget.Space
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.OndemandVideo
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import com.pixeldev.composetv.data.remote.response.MovieDetailsDTO
import com.pixeldev.composetv.models.Genre
import com.pixeldev.composetv.utlis.Constants.Companion.BASE_BACKDROP_IMAGE_URL_1280
import com.pixeldev.composetv.utlis.Constants.Companion.BASE_BACKDROP_IMAGE_URL_300
import com.pixeldev.composetv.utlis.Constants.Companion.BASE_POSTER_IMAGE_URL
import com.pixeldev.composetv.utlis.TitleValueText
import com.pixeldev.composetv.utlis.rememberChildPadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieDetails(
    goToMoviePlayer: () -> Unit,
    movieDetails: MovieDetailsDTO
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
            BASE_BACKDROP_IMAGE_URL_1280 + movieDetails.backdropPath,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxWidth(0.55f)) {
            Spacer(modifier = Modifier.height(108.dp))
            Column(
                modifier = Modifier.padding(start = childPadding.start)
            ) {
                MovieLargeTitle(movieTitle = movieDetails.title)

                Column(
                    modifier = Modifier.alpha(0.75f)
                ) {
                    MovieDescription(description = movieDetails.overview ?: "")
                    Spacer(modifier = Modifier.height(8.dp))
                    // IMDb + Duration + Year
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val rating =
                            movieDetails.voteAverage?.let { String.format("%.1f", it) } ?: "-"
                        Text("IMDb $rating", color = Color.White)
                        Text(
                            "${movieDetails.runtime?.div(60)} h ${movieDetails.runtime?.rem(60)} min",
                            color = Color.White
                        )
                        Text(movieDetails.releaseDate.take(4), color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Badges row (X-RAY, HDR, UHD, etc.)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        BadgeChip("HDR")
                        BadgeChip("UHD")
                        BadgeChip("AD")
                    }

                    DotSeparatedRow(
                        modifier = Modifier.padding(top = 20.dp),
                        texts = movieDetails.genres
                    )
                    DirectorScreenplayMusicRow(
                        director = "director",
                        screenplay = "screenplay",
                        music = "music"
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    WatchTrailerButton(
                        title = "Watch Now",
                        imageVector = Icons.Outlined.PlayArrow,
                        modifier = Modifier.onFocusChanged {
                            if (it.isFocused) {
                                coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
                            }
                        },
                        goToMoviePlayer = goToMoviePlayer
                    )

                    WatchTrailerButton(
                        title = "Watch Trailer",
                        imageVector = Icons.Outlined.OndemandVideo,
                        modifier = Modifier.onFocusChanged {
                            if (it.isFocused) {
                                coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
                            }
                        },
                        goToMoviePlayer = goToMoviePlayer
                    )

                    WatchTrailerButton(
                        title = "Add to favourite",
                        imageVector = Icons.Outlined.FavoriteBorder,
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
}

@Composable
private fun WatchTrailerButton(
    modifier: Modifier = Modifier,
    goToMoviePlayer: () -> Unit,
    title: String,
    imageVector: ImageVector
) {
    Button(
        onClick = goToMoviePlayer,
        modifier = modifier.padding(top = 24.dp),
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        shape = ButtonDefaults.shape(shape = ShapeDefaults.ExtraSmall)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = title,
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
        color = Color.White,
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
    imageUrl: String,
    modifier: Modifier = Modifier,
    gradientColor: Color = MaterialTheme.colorScheme.background,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
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
                    startX = size.width,
                    endX = size.width / 2f
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


@Composable
fun BadgeChip(text: String) {
    Box(
        modifier = Modifier
            .background(Color.DarkGray, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text, style = MaterialTheme.typography.labelSmall, color = Color.White)
    }
}

@Composable
fun GenreChip(name: String) {
    Box(
        modifier = Modifier
            .border(1.dp, Color.Gray, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(name, style = MaterialTheme.typography.bodySmall, color = Color.White)
    }
}
