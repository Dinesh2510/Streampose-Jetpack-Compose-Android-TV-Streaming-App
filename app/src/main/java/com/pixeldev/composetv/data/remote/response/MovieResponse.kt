package com.pixeldev.composetv.data.remote.response

import com.pixeldev.composetv.models.Movies
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Serializable
data class MovieResponse(

    @SerialName("page") var page: Int? = null,
    @SerialName("results") var results: ArrayList<Movies> = arrayListOf(),
    @SerialName("total_pages") var totalPages: Int? = null,
    @SerialName("total_results") var totalResults: Int? = null

)