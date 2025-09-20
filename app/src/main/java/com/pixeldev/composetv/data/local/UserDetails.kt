package com.pixeldev.composetv.data.local

import androidx.datastore.core.Serializer
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

data class UserDetails(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val password: String
)

data class SearchHistory(
    val searchTerms: List<String> = emptyList()
)
