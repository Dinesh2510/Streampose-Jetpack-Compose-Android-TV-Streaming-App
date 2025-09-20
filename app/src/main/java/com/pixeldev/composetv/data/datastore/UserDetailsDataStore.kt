package com.pixeldev.composetv.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pixeldev.composetv.data.local.SearchHistory
import com.pixeldev.composetv.data.local.UserDetails
import androidx.datastore.preferences.preferencesDataStore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val USER_FIRST_NAME = stringPreferencesKey("first_name")
val USER_LAST_NAME = stringPreferencesKey("last_name")
val USER_PHONE_NUMBER = stringPreferencesKey("phone_number")
val USER_EMAIL = stringPreferencesKey("email")
val USER_PASSWORD = stringPreferencesKey("password")

val SEARCH_HISTORY = stringPreferencesKey("search_history")


val Context.userDetailsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_prefs"
)


class UserDetailsDataStore @Inject constructor(
    private val context: Context
) {
    // Read user details
    val userDetails: Flow<UserDetails> = context.userDetailsDataStore.data
        .map { preferences ->
            UserDetails(
                firstName = preferences[USER_FIRST_NAME] ?: "",
                lastName = preferences[USER_LAST_NAME] ?: "",
                phoneNumber = preferences[USER_PHONE_NUMBER] ?: "",
                email = preferences[USER_EMAIL] ?: "",
                password = preferences[USER_PASSWORD] ?: ""
            )
        }

    // Write user details
    suspend fun saveUserDetails(userDetails: UserDetails) {
        context.userDetailsDataStore.edit { preferences ->
            preferences[USER_FIRST_NAME] = userDetails.firstName
            preferences[USER_LAST_NAME] = userDetails.lastName
            preferences[USER_PHONE_NUMBER] = userDetails.phoneNumber
            preferences[USER_EMAIL] = userDetails.email
            preferences[USER_PASSWORD] = userDetails.password
        }
    }

    // Delete user details
    suspend fun clearUserDetails() {
        context.userDetailsDataStore.edit { preferences ->
            preferences.remove(USER_FIRST_NAME)
            preferences.remove(USER_LAST_NAME)
            preferences.remove(USER_PHONE_NUMBER)
            preferences.remove(USER_EMAIL)
            preferences.remove(USER_PASSWORD)
        }
    }

    // Manage search history
    val searchHistory: Flow<SearchHistory> = context.userDetailsDataStore.data
        .map { preferences ->
            val searchHistoryList = preferences[SEARCH_HISTORY]?.split(",") ?: emptyList()
            SearchHistory(searchHistoryList)
        }

    suspend fun addSearchTerm(searchTerm: String) {
        context.userDetailsDataStore.edit { preferences ->
            val updatedSearchHistory = preferences[SEARCH_HISTORY]?.split(",")?.toMutableList() ?: mutableListOf()
            updatedSearchHistory.add(searchTerm)
            preferences[SEARCH_HISTORY] = updatedSearchHistory.joinToString(",")
        }
    }

    suspend fun clearSearchHistory() {
        context.userDetailsDataStore.edit { preferences ->
            preferences.remove(SEARCH_HISTORY)
        }
    }
}
