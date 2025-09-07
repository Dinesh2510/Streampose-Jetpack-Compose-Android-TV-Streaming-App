package com.pixeldev.composetv.screens.movie

import android.util.Log
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import androidx.tv.material3.*
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pixeldev.composetv.R
@Composable
fun MoviesContentScreen() {
    val dummyMovieCategories = listOf(
        "Action Movies" to listOf(
            Movie("1", "Shadow Hunter", "https://picsum.photos/id/1015/800/450", "Action", "UA 13+", "2022", "2h 21m", "A relentless detective unravels a web of secrets as he hunts a mysterious assassin lurking in the shadows."),
            Movie("2", "Mad Max", "https://picsum.photos/id/1023/800/450", "Action", "UA", "2021", "1h 58m", "Survivors clash in a post-apocalyptic wasteland."),
            Movie("3", "John Wick", "https://picsum.photos/id/1032/800/450", "Action", "UA 16+", "2020", "2h 10m", "A retired hitman seeks vengeance.")
        ),
        "Comedy Movies" to listOf(
            Movie("4", "The Hangover", "https://picsum.photos/id/1045/800/450", "Comedy", "UA", "2009", "1h 40m", "A bachelor party goes hilariously wrong."),
            Movie("5", "Superbad", "https://picsum.photos/id/1050/800/450", "Comedy", "UA 13+", "2007", "1h 53m", "Teens try to score alcohol for a party."),
            Movie("6", "Step Brothers", "https://picsum.photos/id/1055/800/450", "Comedy", "UA", "2008", "1h 38m", "Two grown men become stepbrothers.")
        )
    )

    val focusedMovie = remember { mutableStateOf(dummyMovieCategories.first().second.first()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        item {
            TopHeroSection(focusedMovie.value)
        }

        dummyMovieCategories.forEach { (categoryTitle, movies) ->
            item {
                Text(
                    text = categoryTitle,
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(movies) { movie ->
                        MovieCard(movie = movie) {
                            focusedMovie.value = movie
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopHeroSection(movie: Movie) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(movie.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = 200f
                ))
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = movie.rating,
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(text = movie.year, color = Color.Gray)
                Spacer(Modifier.width(8.dp))
                Text(text = movie.duration, color = Color.Gray)
            }

            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = movie.description,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
@Composable
fun MovieCard(
    movie: Movie,
    onFocus: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(180.dp, 100.dp)
            .onFocusChanged {
                if (it.isFocused) onFocus()
            }, onClick = {}
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movie.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

data class Movie(
    val id: String,
    val title: String,
    val imageUrl: String,
    val category: String,
    val rating: String,
    val year: String,
    val duration: String,
    val description: String
)
