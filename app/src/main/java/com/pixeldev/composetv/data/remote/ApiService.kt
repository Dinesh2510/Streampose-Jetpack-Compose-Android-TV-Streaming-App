package com.pixeldev.composetv.data.remote


import com.pixeldev.composetv.utlis.Constants.Companion.API_KEY
import com.pixeldev.composetv.data.remote.response.CastResponse
import com.pixeldev.composetv.data.remote.response.GenreResponse
import com.pixeldev.composetv.data.remote.response.MovieDetailsDTO
import com.pixeldev.composetv.data.remote.response.MovieResponse
import com.pixeldev.composetv.data.remote.response.MultiSearchResponse

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import javax.inject.Inject
/*interface ApiService {
    suspend fun loginUser(request: LoginRequest): LoginResponse
}*/
class ApiService @Inject constructor(
    private val client: HttpClient
) {

    /**** Movies ****/

suspend fun getTrendingMovies(
    page: Int = 0,
    apiKey: String = API_KEY,
    language: String = "en"
): MovieResponse {
    return client.get {
        url("trending/movie/week")
        parameter("page", page)
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()
}

    suspend fun getPopularMovies(
        page: Int = 0,
        apiKey: String = API_KEY,
        language: String = "en"
    ): MovieResponse {
        return client.get {
            url("movie/popular")
            parameter("page", page)
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
    }

    suspend fun getTopRatedMovies(
        page: Int = 0,
        apiKey: String = API_KEY,
        language: String = "en"
    ): MovieResponse {
        return client.get {
            url("movie/top_rated")
            parameter("page", page)
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
    }

    suspend fun getNowPlayingMovies(
        page: Int = 0,
        apiKey: String = API_KEY,
        language: String = "en"
    ): MovieResponse {
        return client.get {
            url("movie/now_playing")
            parameter("page", page)
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
    }

    suspend fun getUpcomingMovies(
        page: Int = 0,
        apiKey: String = API_KEY,
        language: String = "en"
    ): MovieResponse {
        return client.get {
            url("movie/upcoming")
            parameter("page", page)
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
    }

    suspend fun getRecommendedMovies(
        movieId: Int,
        page: Int = 0,
        apiKey: String = API_KEY,
        language: String = "en"
    ): MovieResponse {
        return client.get {
            url("movie/$movieId/recommendations")
            parameter("page", page)
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
    }

    suspend fun getGenreWiseMovieList(
        genresId: Int,
        page: Int = 1,
        apiKey: String = API_KEY,
        language: String = "en"
    ): MovieResponse {
        return client.get {
            url("discover/movie")
            parameter("with_genres", genresId)
            parameter("page", page)
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
    }

    suspend fun getSimilarMovies(
        filmId: Int,
        page: Int = 1,
        apiKey: String = API_KEY,
        language: String = "en"
    ): MovieResponse {
        return client.get {
            url("movie/$filmId/similar")
            parameter("page", page)
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
    }

    suspend fun getDiscoverMovies(
        page: Int = 0,
        gteReleaseDate: String = "1940-01-01",
        lteReleaseDate: String = "1981-01-01",
        apiKey: String = API_KEY,
        language: String = "en",
        sortBy: String = "vote_count.desc"
    ): MovieResponse {
        return client.get {
            url("discover/movie")
            parameter("page", page)
            parameter("primary_release_date.gte", gteReleaseDate)
            parameter("primary_release_date.lte", lteReleaseDate)
            parameter("api_key", apiKey)
            parameter("language", language)
            parameter("sort_by", sortBy)
        }.body()
    }

    suspend fun getMoviesDetails(
        movieId: Int,
        appendToResponse: String = "videos,credits",
        apiKey: String = API_KEY,
        language: String = "en"
    ): MovieDetailsDTO {
        return client.get {
            url("movie/$movieId")
            parameter("append_to_response", appendToResponse)
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
    }

    suspend fun getMovieCast(
        filmId: Int,
        apiKey: String = API_KEY
    ): CastResponse {
        return client.get {
            url("movie/$filmId/credits")
            parameter("api_key", apiKey)
        }.body()
    }

    suspend fun getMovieGenres(
        apiKey: String = API_KEY,
        language: String = "en"
    ): GenreResponse {
        return client.get {
            url("genre/movie/list")
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
    }

    suspend fun multiSearch(
        searchParams: String,
        page: Int = 0,
        includeAdult: Boolean = true,
        apiKey: String = API_KEY,
        language: String = "en"
    ): MultiSearchResponse {
        return client.get {
            url("search/multi")
            parameter("query", searchParams)
            parameter("page", page)
            parameter("include_adult", includeAdult)
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
    }


    /**** Tv Shows ****/

    suspend fun getTvShowGenres(
        apiKey: String = API_KEY,
        language: String = "en-US"
    ): GenreResponse {
        return client.get {
            url("genre/tv/list")
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
    }

    suspend fun getTvShowCast(
        filmId: Int,
        apiKey: String = API_KEY
    ): CastResponse {
        return client.get {
            url("tv/$filmId/credits")
            parameter("api_key", apiKey)
        }.body()
    }

    suspend fun getSimilarTvShows(
        filmId: Int,
        page: Int = 0,
        apiKey: String = API_KEY,
        language: String = "en-US"
    ): MovieResponse {
        return client.get {
            url("tv/$filmId/similar")
            parameter("page", page)
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
    }

    suspend fun getTrendingTvSeries(
        page: Int = 0,
        apiKey: String = API_KEY,
        language: String = "en-US"
    ): MovieResponse {
        return client.get {
            url("trending/tv/week")
            parameter("page", page)
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
    }

    suspend fun getPopularTvShows(
        page: Int = 0,
        apiKey: String = API_KEY,
        language: String = "en-US"
    ): MovieResponse {
        return client.get {
            url("tv/popular")
            parameter("page", page)
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
    }

    suspend fun getTopRatedTvShows(
        page: Int = 0,
        apiKey: String = API_KEY,
        language: String = "en-US"
    ): MovieResponse {
        return client.get {
            url("tv/top_rated")
            parameter("page", page)
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
    }

    suspend fun getRecommendedTvShows(
        filmId: Int,
        page: Int = 0,
        apiKey: String = API_KEY,
        language: String = "en-US"
    ): MovieResponse {
        return client.get {
            url("tv/$filmId/recommendations")
            parameter("page", page)
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
    }

    suspend fun getDiscoverTvShows(
        page: Int = 0,
        gteFirstAirDate: String = "1940-01-01",
        lteFirstAirDate: String = "1981-01-01",
        apiKey: String = API_KEY,
        language: String = "en-US",
        sortBy: String = "vote_count.desc"
    ): MovieResponse {
        return client.get {
            url("discover/tv")
            parameter("page", page)
            parameter("first_air_date.gte", gteFirstAirDate)
            parameter("first_air_date.lte", lteFirstAirDate)
            parameter("api_key", apiKey)
            parameter("language", language)
            parameter("sort_by", sortBy)
        }.body()
    }

}
