package com.pixeldev.composetv.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Credits(
    @SerialName("cast")
    val cast: List<CreditCast>,
    @SerialName("crew")
    val crew: List<Crew>
)