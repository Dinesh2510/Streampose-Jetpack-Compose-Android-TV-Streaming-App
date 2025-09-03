package com.pixeldev.composetv.utlis

import com.pixeldev.composetv.BuildConfig

class Constants {

    companion object {

        const val BASE_URL = "api.themoviedb.org/"
        const val API_KEY =  BuildConfig.API_KEY
        const val BASE_BACKDROP_IMAGE_URL = "https://image.tmdb.org/t/p/w780/"
        const val BASE_POSTER_IMAGE_URL = "https://image.tmdb.org/t/p/w500/"

        const val nowPlayingAllListScreen = "nowPlayingAllListScreen"
        const val popularAllListScreen = "popularAllListScreen"
        const val discoverListScreen = "DiscoverListScreen"
        const val upcomingListScreen = "upcomingListScreen"
        const val similarListScreen = "similarListing"
        const val genreWiseMovie = "genreWiseMovie"


    }

}