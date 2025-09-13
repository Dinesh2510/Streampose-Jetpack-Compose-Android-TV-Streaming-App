package com.pixeldev.composetv.di


import com.pixeldev.composetv.data.remote.ApiService
import com.pixeldev.composetv.data.repository.HomeRepository
import com.pixeldev.composetv.data.repository.HomeRepositoryImpl
import com.pixeldev.composetv.data.repository.ShowRepository
import com.pixeldev.composetv.data.repository.ShowRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideHomeRepository(
        apiService: ApiService
    ): HomeRepository {
        return HomeRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideShowRepository(
        apiService: ApiService
    ): ShowRepository {
        return ShowRepositoryImpl(apiService)
    }


}