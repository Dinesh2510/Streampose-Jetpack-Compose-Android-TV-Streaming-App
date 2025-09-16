package com.pixeldev.composetv.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.pixeldev.composetv.data.local.WatchListModel

@AutoMigration(from = 1, to = 2)
@Database(version = 1, entities = [WatchListModel::class], exportSchema = false)
abstract class MovieDatabase():RoomDatabase() {
    abstract fun movieDao(): MovieDao
}