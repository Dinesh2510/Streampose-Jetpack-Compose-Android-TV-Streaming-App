package com.pixeldev.composetv.data.remote.response

import com.pixeldev.composetv.models.Search
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class MultiSearchResponse(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<Search>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)
