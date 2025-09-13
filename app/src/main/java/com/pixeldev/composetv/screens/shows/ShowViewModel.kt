package com.pixeldev.composetv.screens.shows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixeldev.composetv.data.remote.response.MovieResponse

import com.pixeldev.composetv.data.repository.ShowRepository
import com.pixeldev.composetv.utlis.MovieState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject
@HiltViewModel
class ShowViewModel @Inject constructor(
    private val repository: ShowRepository
) : ViewModel() {

    // Holds the state for each category by name
    private val _categoriesState = MutableStateFlow<Map<String, MovieState<Any?>>>(emptyMap())
    val categoriesState: StateFlow<Map<String, MovieState<Any?>>> = _categoriesState

    fun fetchAllTvShows() {
        viewModelScope.launch {
            _categoriesState.emit(emptyMap()) // reset or loading state if needed

            supervisorScope {
                val trendingDeferred = async {
                    runCatching { repository.getTrendingTvShows().first() }
                        .fold(
                            onSuccess = { MovieState.Success(it) },
                            onFailure = { MovieState.Error(it.localizedMessage ?: "Error fetching trending") }
                        )
                }

                val popularDeferred = async {
                    runCatching { repository.getPopularTvShows().first() }
                        .fold(
                            onSuccess = { MovieState.Success(it) },
                            onFailure = { MovieState.Error(it.localizedMessage ?: "Error fetching popular") }
                        )
                }

                val topRatedDeferred = async {
                    runCatching { repository.getTopRatedTvShows().first() }
                        .fold(
                            onSuccess = { MovieState.Success(it) },
                            onFailure = { MovieState.Error(it.localizedMessage ?: "Error fetching top rated") }
                        )
                }



                val discoverDeferred = async {
                    runCatching { repository.getDiscoverTvShows().first() }
                        .fold(
                            onSuccess = { MovieState.Success(it) },
                            onFailure = { MovieState.Error(it.localizedMessage ?: "Error fetching discover") }
                        )
                }

                val genresDeferred = async {
                    runCatching { repository.getTvShowGenres().first() }
                        .fold(
                            onSuccess = { MovieState.Success(it) },
                            onFailure = { MovieState.Error(it.localizedMessage ?: "Error fetching genres") }
                        )
                }

                // Await all results
                val categoriesMap = mapOf(
                    "Trending" to trendingDeferred.await(),
                    "Popular" to popularDeferred.await(),
                    "Top Rated" to topRatedDeferred.await(),
                    "Discover" to discoverDeferred.await(),
                    "Genres" to genresDeferred.await()
                )

                _categoriesState.emit(categoriesMap)
            }
        }
    }

    // Trending TV Shows
    private val _trendingTvShows = MutableStateFlow<MovieState<MovieResponse?>>(MovieState.Loading)
    val trendingTvShows: StateFlow<MovieState<MovieResponse?>> = _trendingTvShows

    fun fetchTrendingTvShows() {
        viewModelScope.launch {
            try {
                val response = repository.getTrendingTvShows().first()
                _trendingTvShows.emit(MovieState.Success(response))
            } catch (e: Exception) {
                _trendingTvShows.emit(MovieState.Error("Error fetching trending shows"))
            }
        }
    }
}


























    /*

        // Trending TV Shows
        private val _trendingTvShows = MutableStateFlow<MovieState<MovieResponse?>>(MovieState.Loading)
        val trendingTvShows: StateFlow<MovieState<MovieResponse?>> = _trendingTvShows

        fun fetchTrendingTvShows() {
            viewModelScope.launch {
                try {
                    val response = repository.getTrendingTvShows().first()
                    _trendingTvShows.emit(MovieState.Success(response))
                } catch (e: Exception) {
                    _trendingTvShows.emit(MovieState.Error("Error fetching trending shows"))
                }
            }
        }

        // Popular TV Shows
        private val _popularTvShows = MutableStateFlow<MovieState<MovieResponse?>>(MovieState.Loading)
        val popularTvShows: StateFlow<MovieState<MovieResponse?>> = _popularTvShows

        fun fetchPopularTvShows() {
            viewModelScope.launch {
                try {
                    val response = repository.getPopularTvShows().first()
                    _popularTvShows.emit(MovieState.Success(response))
                } catch (e: Exception) {
                    _popularTvShows.emit(MovieState.Error("Error fetching popular shows"))
                }
            }
        }

        // Top Rated TV Shows
        private val _topRatedTvShows = MutableStateFlow<MovieState<MovieResponse?>>(MovieState.Loading)
        val topRatedTvShows: StateFlow<MovieState<MovieResponse?>> = _topRatedTvShows

        fun fetchTopRatedTvShows() {
            viewModelScope.launch {
                try {
                    val response = repository.getTopRatedTvShows().first()
                    _topRatedTvShows.emit(MovieState.Success(response))
                } catch (e: Exception) {
                    _topRatedTvShows.emit(MovieState.Error("Error fetching top rated shows"))
                }
            }
        }

        // Recommended TV Shows
        private val _recommendedTvShows = MutableStateFlow<MovieState<MovieResponse?>>(MovieState.Loading)
        val recommendedTvShows: StateFlow<MovieState<MovieResponse?>> = _recommendedTvShows

        fun fetchRecommendedTvShows(filmId: Int) {
            viewModelScope.launch {
                try {
                    val response = repository.getRecommendedTvShows(filmId).first()
                    _recommendedTvShows.emit(MovieState.Success(response))
                } catch (e: Exception) {
                    _recommendedTvShows.emit(MovieState.Error("Error fetching recommended shows"))
                }
            }
        }

        // Discover TV Shows
        private val _discoverTvShows = MutableStateFlow<MovieState<MovieResponse?>>(MovieState.Loading)
        val discoverTvShows: StateFlow<MovieState<MovieResponse?>> = _discoverTvShows

        fun fetchDiscoverTvShows() {
            viewModelScope.launch {
                try {
                    val response = repository.getDiscoverTvShows().first()
                    _discoverTvShows.emit(MovieState.Success(response))
                } catch (e: Exception) {
                    _discoverTvShows.emit(MovieState.Error("Error fetching discover shows"))
                }
            }
        }

        // TV Show Genres
        private val _tvShowGenres = MutableStateFlow<MovieState<GenreResponse?>>(MovieState.Loading)
        val tvShowGenres: StateFlow<MovieState<GenreResponse?>> = _tvShowGenres

        fun fetchTvShowGenres() {
            viewModelScope.launch {
                try {
                    val response = repository.getTvShowGenres().first()
                    _tvShowGenres.emit(MovieState.Success(response))
                } catch (e: Exception) {
                    _tvShowGenres.emit(MovieState.Error("Error fetching genres"))
                }
            }
        }

        // TV Show Cast
        private val _tvShowCast = MutableStateFlow<MovieState<CastResponse?>>(MovieState.Loading)
        val tvShowCast: StateFlow<MovieState<CastResponse?>> = _tvShowCast

        fun fetchTvShowCast(filmId: Int) {
            viewModelScope.launch {
                try {
                    val response = repository.getTvShowCast(filmId).first()
                    _tvShowCast.emit(MovieState.Success(response))
                } catch (e: Exception) {
                    _tvShowCast.emit(MovieState.Error("Error fetching cast"))
                }
            }
        }

        // Similar TV Shows
        private val _similarTvShows = MutableStateFlow<MovieState<MovieResponse?>>(MovieState.Loading)
        val similarTvShows: StateFlow<MovieState<MovieResponse?>> = _similarTvShows

        fun fetchSimilarTvShows(filmId: Int) {
            viewModelScope.launch {
                try {
                    val response = repository.getSimilarTvShows(filmId).first()
                    _similarTvShows.emit(MovieState.Success(response))
                } catch (e: Exception) {
                    _similarTvShows.emit(MovieState.Error("Error fetching similar shows"))
                }
            }
        }*/
