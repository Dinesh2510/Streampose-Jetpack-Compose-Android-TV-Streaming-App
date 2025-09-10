package com.pixeldev.composetv.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class ProductionCompany(
    @SerialName("id")
    val id: Int,
    @SerialName("logo_path")
    val logoPath: String?,
    @SerialName("name")
    val name: String,
    @SerialName("origin_country")
    val originCountry: String
): Parcelable
