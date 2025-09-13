package com.pixeldev.composetv.data.repository

import com.pixeldev.composetv.data.remote.response.CastResponse
import com.pixeldev.composetv.data.remote.response.GenreResponse
import com.pixeldev.composetv.data.remote.response.MovieResponse
import kotlinx.coroutines.flow.Flow

interface ShowRepository {
    fun getTrendingTvShows(): Flow<MovieResponse>
    fun getPopularTvShows(): Flow<MovieResponse>
    fun getTopRatedTvShows(): Flow<MovieResponse>
    fun getRecommendedTvShows(filmId: Int): Flow<MovieResponse>
    fun getDiscoverTvShows(): Flow<MovieResponse>
    fun getTvShowGenres(): Flow<GenreResponse>
    fun getTvShowCast(filmId: Int): Flow<CastResponse>
    fun getSimilarTvShows(filmId: Int): Flow<MovieResponse>
}
