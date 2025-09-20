package com.pixeldev.composetv.data.repository


import com.pixeldev.composetv.data.datastore.UserDetailsDataStore
import com.pixeldev.composetv.data.local.SearchHistory
import com.pixeldev.composetv.data.local.UserDetails
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDetailsDataStore: UserDetailsDataStore
) {
    // User Details operations
    val userDetails: Flow<UserDetails> = userDetailsDataStore.userDetails

    suspend fun saveUserDetails(userDetails: UserDetails) {
        userDetailsDataStore.saveUserDetails(userDetails)
    }

    suspend fun clearUserDetails() {
        userDetailsDataStore.clearUserDetails()
    }

    // Search History operations
    val searchHistory: Flow<SearchHistory> = userDetailsDataStore.searchHistory

    suspend fun addSearchTerm(searchTerm: String) {
        userDetailsDataStore.addSearchTerm(searchTerm)
    }

    suspend fun clearSearchHistory() {
        userDetailsDataStore.clearSearchHistory()
    }
}
