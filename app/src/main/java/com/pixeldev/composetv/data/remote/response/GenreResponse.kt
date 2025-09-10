package com.pixeldev.composetv.data.remote.response

import com.pixeldev.composetv.models.Genre
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    @SerialName("genres")
    val genres: List<Genre>
)