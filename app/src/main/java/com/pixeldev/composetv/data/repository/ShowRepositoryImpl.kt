package com.pixeldev.composetv.data.repository


import com.pixeldev.composetv.data.remote.ApiService
import com.pixeldev.composetv.data.remote.response.CastResponse
import com.pixeldev.composetv.data.remote.response.GenreResponse
import com.pixeldev.composetv.data.remote.response.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ShowRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ShowRepository {

    override fun getTrendingTvShows(): Flow<MovieResponse> = flow {
        emit(apiService.getTrendingTvSeries())
    }.flowOn(Dispatchers.IO)

    override fun getPopularTvShows(): Flow<MovieResponse> = flow {
        emit(apiService.getPopularTvShows())
    }.flowOn(Dispatchers.IO)

    override fun getTopRatedTvShows(): Flow<MovieResponse> = flow {
        emit(apiService.getTopRatedTvShows())
    }.flowOn(Dispatchers.IO)

    override fun getRecommendedTvShows(filmId: Int): Flow<MovieResponse> = flow {
        emit(apiService.getRecommendedTvShows(filmId))
    }.flowOn(Dispatchers.IO)

    override fun getDiscoverTvShows(): Flow<MovieResponse> = flow {
        emit(apiService.getDiscoverTvShows())
    }.flowOn(Dispatchers.IO)

    override fun getTvShowGenres(): Flow<GenreResponse> = flow {
        emit(apiService.getTvShowGenres())
    }.flowOn(Dispatchers.IO)

    override fun getTvShowCast(filmId: Int): Flow<CastResponse> = flow {
        emit(apiService.getTvShowCast(filmId))
    }.flowOn(Dispatchers.IO)

    override fun getSimilarTvShows(filmId: Int): Flow<MovieResponse> = flow {
        emit(apiService.getSimilarTvShows(filmId))
    }.flowOn(Dispatchers.IO)
}
