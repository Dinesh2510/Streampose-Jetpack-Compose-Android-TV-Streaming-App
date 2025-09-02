package com.pixeldev.composetv.data.remote.response

import com.pixeldev.composetv.models.Genre
import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("genres")
    val genres: List<Genre>
)