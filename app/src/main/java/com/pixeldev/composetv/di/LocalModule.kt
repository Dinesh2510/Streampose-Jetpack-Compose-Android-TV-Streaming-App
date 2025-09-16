package com.pixeldev.composetv.di

import android.content.Context
import androidx.room.Room
import com.pixeldev.composetv.data.local.MovieDao
import com.pixeldev.composetv.data.local.MovieDatabase
import com.pixeldev.composetv.data.repository.MyListMovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context): MovieDatabase =
        Room.databaseBuilder(context, MovieDatabase::class.java, "watch_list_table")
            .fallbackToDestructiveMigration().build()

    @Provides
    fun provideMovieDao(movieDatabase: MovieDatabase) = movieDatabase.movieDao()

    @Singleton
    @Provides
    fun provideMyListRepository(movieDao: MovieDao): MyListMovieRepository =
        MyListMovieRepository(movieDao = movieDao)

}