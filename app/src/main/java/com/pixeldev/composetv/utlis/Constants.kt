package com.pixeldev.composetv.utlis

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.pixeldev.composetv.BuildConfig

class Constants {

    companion object {
//https://www.themoviedb.org/talk/5aeaaf56c3a3682ddf0010de
        const val BASE_URL = "api.themoviedb.org/3"
        const val API_KEY =  BuildConfig.API_KEY
        const val BASE_BACKDROP_IMAGE_URL_300 = "https://image.tmdb.org/t/p/w300/"
        const val BASE_BACKDROP_IMAGE_URL_780 = "https://image.tmdb.org/t/p/w780/"
        const val BASE_BACKDROP_IMAGE_URL_1280 = "https://image.tmdb.org/t/p/w1280/"
        const val BASE_POSTER_IMAGE_URL = "https://image.tmdb.org/t/p/w500/"

        const val nowPlayingAllListScreen = "nowPlayingAllListScreen"
        const val popularAllListScreen = "popularAllListScreen"
        const val discoverListScreen = "DiscoverListScreen"
        const val upcomingListScreen = "upcomingListScreen"
        const val similarListScreen = "similarListing"
        const val genreWiseMovie = "genreWiseMovie"

        val StreamposeGradient = Brush.linearGradient(
            colors = listOf(
                Color(0xFF9C27FF), // Purple
                Color(0xFF2196F3)  // Blue
            ),
            start = Offset(0f, 0f), // top-left
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY) // bottom-right
        )
        val StreamposeBackground = Brush.linearGradient(
            colors = listOf(
                Color(0xFF0A0F1F), // Dark Navy
                Color(0xFF000000)  // Black
            ),
            start = Offset(0f, 0f), // top-left
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY) // bottom-right
        )
    }

}