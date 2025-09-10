package com.pixeldev.composetv.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class TvShow(
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("genre_ids")
    val genreIds: List<Int>?,
    @SerialName("genres")
    val genres: List<Genre>?,
    @SerialName("id")
    val id: Int,
    @SerialName("imdb_id")
    val imdbId: String?,
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompany>?,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_title")
    val originalTitle: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("runtime")
    val runtime: Int,
    @SerialName("title")
    val title: String,
    @SerialName("video")
    val video: Boolean,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int
): Parcelable
