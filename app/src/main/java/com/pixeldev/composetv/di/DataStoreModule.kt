package com.pixeldev.composetv.di

import android.content.Context
import com.pixeldev.composetv.data.datastore.UserDetailsDataStore
import com.pixeldev.composetv.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUserDetailsDataStore(@ApplicationContext context: Context): UserDetailsDataStore {
        return UserDetailsDataStore(context)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDetailsDataStore: UserDetailsDataStore): UserRepository {
        return UserRepository(userDetailsDataStore)
    }
}
