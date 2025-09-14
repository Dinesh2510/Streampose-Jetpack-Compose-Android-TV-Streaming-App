

/*
/****=================================================================================================*/
Below code immersive listing

@Composable
fun SetMovieData(
    discoveryMovie: MovieResponse?,
    trendingMovie: MovieResponse?,
    nowPlayingMovie: MovieResponse?,
    upcomingMovie: MovieResponse?,
    genresMovie: GenreResponse?,
    popularMovie: LazyPagingItems<Movies>,
    navController: NavHostController
) {
    // Convert genre list to Map for future use (if needed)
    val genreMap = genresMovie?.genres?.associateBy({ it.id }, { it.name }) ?: emptyMap()

    // Convert each API movie list to MediaItems
    val discoveryItems = mapMoviesToMediaItems(discoveryMovie?.results,
        genreMap as Map<Int, String>
    )
    val trendingItems = mapMoviesToMediaItems(trendingMovie?.results, genreMap)
    val nowPlayingItems = mapMoviesToMediaItems(nowPlayingMovie?.results, genreMap)
    val upcomingItems = mapMoviesToMediaItems(upcomingMovie?.results, genreMap)

    // Create rows with title + items
    val mediaRows = listOf(
        MediaRow(1,"Discover", discoveryItems),
        MediaRow(2,"Trending", trendingItems),
        MediaRow(3,"Now Playing", nowPlayingItems),
        MediaRow(4,"Upcoming", upcomingItems)
    )

    // Feed to Immersive UI
    ImmersiveListScreenNEW(mediaRows = mediaRows)
}
    */


