package com.pixeldev.composetv.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.app.movieapp.data.paging.MovieGenrePagingSource
import com.pixeldev.composetv.data.paging.MoviePagingSource
import com.pixeldev.composetv.data.remote.ApiService
import javax.inject.Inject
import com.pixeldev.composetv.models.Movies
import com.pixeldev.composetv.data.remote.response.GenreResponse
import com.pixeldev.composetv.data.remote.response.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class HomeRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : HomeRepository {

    override fun getNowPlayingMoviesRepo(): Flow<MovieResponse> = flow {
        val response = apiService.getNowPlayingMovies(2)
        emit(response)
    }.flowOn(Dispatchers.IO)

    override fun getPopularMoviesRepo(): Flow<MovieResponse> = flow {
        val response = apiService.getPopularMovies(1)
        emit(response)
    }.flowOn(Dispatchers.IO)

    override fun getAllMoviesPagination(tags: String): Pager<Int, Movies> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MoviePagingSource(apiService, tags) }
        )
    }

    override fun getGenresWiseMovieRepo(tags: Int): Pager<Int, Movies> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MovieGenrePagingSource(apiService, tags) }
        )
    }

    override fun getDiscoverMoviesRepo(): Flow<MovieResponse> = flow {
        val response = apiService.getDiscoverMovies(1)
        emit(response)
    }.flowOn(Dispatchers.IO)

    override fun getTrendingMoviesRepo(): Flow<MovieResponse> = flow {
        val response = apiService.getTrendingMovies(1)
        emit(response)
    }.flowOn(Dispatchers.IO)

    override fun getUpcomingMoviesRepo(): Flow<MovieResponse> = flow {
        val response = apiService.getUpcomingMovies(1)
        emit(response)
    }.flowOn(Dispatchers.IO)

    override fun getMovieGenresRepo(): Flow<GenreResponse> = flow {
        val response = apiService.getMovieGenres()
        emit(response)
    }.flowOn(Dispatchers.IO)
}
