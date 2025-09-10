package com.pixeldev.composetv.data.remote.response

import com.pixeldev.composetv.models.Cast
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CastResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("cast")
    val castResult: List<Cast>
)
