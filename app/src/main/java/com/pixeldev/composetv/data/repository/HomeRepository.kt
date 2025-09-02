package com.pixeldev.composetv.data.repository

import androidx.paging.Pager
import com.pixeldev.composetv.models.Movies
import com.pixeldev.composetv.data.remote.response.GenreResponse
import com.pixeldev.composetv.data.remote.response.MovieResponse

import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getNowPlayingMoviesRepo(): Flow<MovieResponse>
    fun getPopularMoviesRepo(): Flow<MovieResponse>
    fun getAllMoviesPagination(tags: String): Pager<Int, Movies>
    fun getGenresWiseMovieRepo(tags: Int): Pager<Int, Movies>
    fun getDiscoverMoviesRepo(): Flow<MovieResponse>
    fun getTrendingMoviesRepo(): Flow<MovieResponse>
    fun getUpcomingMoviesRepo(): Flow<MovieResponse>
    fun getMovieGenresRepo(): Flow<GenreResponse>
}
