package com.pixeldev.composetv.screens.shows

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import androidx.tv.material3.Card
import androidx.tv.material3.Carousel
import androidx.tv.material3.CarouselDefaults
import androidx.tv.material3.CarouselState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.pixeldev.composetv.screens.movie.Movie
fun generateDummyMovies(): List<Movie> {
    return listOf(
        Movie(
            id = "1",
            title = "House of Cards",
            imageUrl = "https://picsum.photos/id/1045/800/450", // Dummy image
            category = "TV Series",
            rating = "4.3",
            year = "2013-2018",
            duration = "45 min",
            description = "A Congressman works with his equally conniving wife to exact revenge on the people who betrayed him."
        ),
        Movie(
            id = "2",
            title = "Bad Boys",
            imageUrl = "https://picsum.photos/id/1045/800/450", // Dummy image
            category = "Action",
            rating = "4.6",
            year = "1995",
            duration = "120 min",
            description = "Two hip detectives protect a witness to a murder while investigating a case of stolen heroin from the evidence storage room."
        ),
        Movie(
            id = "3",
            title = "Godzilla",
            imageUrl = "https://placekitten.com/400/302", // Dummy image
            category = "Sci-Fi",
            rating = "4.5",
            year = "2014",
            duration = "123 min",
            description = "The world is on the brink of collapse as a giant monster threatens humanity, while a group of scientists attempt to prevent its destruction."
        ),
        Movie(
            id = "4",
            title = "Alienoid",
            imageUrl = "https://picsum.photos/id/1045/800/450", // Dummy image
            category = "Sci-Fi",
            rating = "4.2",
            year = "2022",
            duration = "140 min",
            description = "Two worlds collide in a tale of time-travel and alien encounters as humans and extraterrestrials must battle for survival."
        ),
        Movie(
            id = "5",
            title = "Avengers: Endgame",
            imageUrl = "https://picsum.photos/id/1045/800/450", // Dummy image
            category = "Action",
            rating = "4.8",
            year = "2019",
            duration = "181 min",
            description = "The Avengers assemble once again to undo the devastation caused by Thanos, who wiped out half of all life in the universe."
        ),
        Movie(
            id = "6",
            title = "Justice League",
            imageUrl = "https://picsum.photos/id/1045/800/450", // Dummy image
            category = "Superhero",
            rating = "4.0",
            year = "2017",
            duration = "120 min",
            description = "Batman and Wonder Woman work together to assemble a team of superheroes to save the world from an impending alien invasion."
        ),
        Movie(
            id = "7",
            title = "The Dark Knight",
            imageUrl = "https://picsum.photos/id/1045/800/450", // Dummy image
            category = "Action",
            rating = "4.9",
            year = "2008",
            duration = "152 min",
            description = "Batman faces off against the Joker, a criminal mastermind who seeks to create chaos and anarchy in Gotham City."
        ),
        Movie(
            id = "8",
            title = "Inception",
            imageUrl = "https://picsum.photos/id/1045/800/450", // Dummy image
            category = "Sci-Fi",
            rating = "4.7",
            year = "2010",
            duration = "148 min",
            description = "A thief who steals secrets by entering people's dreams is given the task of planting an idea into the mind of a CEO."
        )
    )
}

@Composable
fun AllShowsScreen(navHostController: NavHostController){
    ShowsScreen(generateDummyMovies(), generateDummyMovies(), generateDummyMovies(),generateDummyMovies())

}

@Composable
fun ShowsScreen(
    heroList: List<Movie>,
    recommendedFilms: List<Movie>,
    continueWatching: List<Movie>,
    recommendedSeries: List<Movie>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentPadding = PaddingValues(vertical = 0.dp)
    ) {
        item {
            HeroCarousel(heroList = heroList)
        }
        item {
            HorizontalMovieRow(title = "Recommended Films", movies = recommendedFilms)
        }
        item {
            HorizontalMovieRow(title = "Continue Watching", movies = continueWatching)
        }
        item {
            HorizontalMovieRow(title = "Recommended Series", movies = recommendedSeries)
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HeroCarousel(heroList: List<Movie>) {
    val carouselState = remember { CarouselState(initialActiveItemIndex = 0) }

    Carousel(
        itemCount = heroList.size,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),  // adjust height as needed
        carouselState = carouselState,
        autoScrollDurationMillis = CarouselDefaults.TimeToDisplayItemMillis,  // default or custom
        contentTransformStartToEnd = fadeIn(tween(durationMillis = 1000)).togetherWith(
            fadeOut(tween(durationMillis = 1000))
        ),
        contentTransformEndToStart = fadeIn(tween(durationMillis = 1000)).togetherWith(
            fadeOut(tween(durationMillis = 1000))
        ),
        carouselIndicator = {
            // default indicator; can customize dots or style
            CarouselDefaults.IndicatorRow(
                itemCount = heroList.size,
                activeItemIndex = carouselState.activeItemIndex,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }
    ) { index ->
        val movie = heroList[index]
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            AsyncImage(
                model = movie.imageUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Overlay text etc
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = movie.title,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.description,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "⭐ 4.5 · 2025",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun HorizontalMovieRow(title: String, movies: List<Movie>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            modifier = Modifier.padding(start = 32.dp, top = 8.dp),
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        TvLazyRow(movies = movies)
    }
}

@Composable
fun TvLazyRow(movies: List<Movie>) {
    // tvFoundation provides TvLazyRow etc, or you can use LazyRow from Compose tv
   LazyRow(
       modifier = Modifier
           .fillMaxWidth(),
       horizontalArrangement = Arrangement.spacedBy(24.dp),
       contentPadding = PaddingValues(horizontal = 32.dp)
    ) {
        items(movies) { movie ->
            CustomCard(
                imageUrl = movie.imageUrl,
                onClick = {  }
            )
        }
    }
}
@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageUrl: String,
    cardAspectRatio: Float = 9f / 16f // Adjusting to 9:16 aspect ratio
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .width(160.dp).height(250.dp)
           // .aspectRatio(cardAspectRatio) // Ensures the card follows 9:16 aspect ratio
            .background(Color.Transparent, RoundedCornerShape(16.dp))
            .padding(bottom = 8.dp),
    ) {
        AsyncImage(
            model = imageUrl, contentDescription = "Image",
            modifier = Modifier
                .fillMaxSize()
                .height(110.dp) // Adjust the height of image based on the aspect ratio
                .aspectRatio(cardAspectRatio), // Ensure the image maintains the aspect ratio
            contentScale = ContentScale.Crop, // Better for cropping and fitting
            alignment = Alignment.Center,
        )

    }
}

@Composable
fun MovieCard(movie: Movie) {
    var isFocused by remember { mutableStateOf(false) }
    // Focus handling
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(240.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .padding(4.dp)
    ) {
        AsyncImage(
            model = movie.imageUrl,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .then(if (isFocused) Modifier.background(Color.White.copy(alpha = 0.2f)) else Modifier)
                // optionally add scale on focus etc
        )
        // Title overlay, rating etc, as per design
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = movie.title,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
           /* val rating = String.format("%.1f", movie.rating)
            val year = movie.year.take(4)*/
            Text(
                text = "⭐ 4.2 · 2025",
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}
