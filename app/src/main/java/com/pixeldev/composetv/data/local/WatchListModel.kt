package com.pixeldev.composetv.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watch_list_table")
data class WatchListModel(
    @PrimaryKey val mediaId: Int,
    val imagePosterPath: String?,
    val title: String,
    val releaseDate: String,
    val rating: Double,
    val addedOn: String,
    val imageBackDropPath: String,
    val description: String,
)