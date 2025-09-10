package com.pixeldev.composetv.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Cast(
    @SerialName("known_for_department")
    val department: String,
    @SerialName("name")
    val name: String,
    @SerialName("profile_path")
    val profilePath: String?
): Parcelable
