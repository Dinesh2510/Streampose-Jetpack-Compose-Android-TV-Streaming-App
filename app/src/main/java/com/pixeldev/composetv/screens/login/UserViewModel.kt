package com.pixeldev.composetv.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixeldev.composetv.data.local.SearchHistory
import com.pixeldev.composetv.data.local.UserDetails
import com.pixeldev.composetv.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val userDetails: Flow<UserDetails> = userRepository.userDetails
    val searchHistory: Flow<SearchHistory> = userRepository.searchHistory

    fun saveUserDetails(userDetails: UserDetails) {
        viewModelScope.launch {
            userRepository.saveUserDetails(userDetails)
        }
    }

    fun clearUserDetails() {
        viewModelScope.launch {
            userRepository.clearUserDetails()
        }
    }

    fun addSearchTerm(searchTerm: String) {
        viewModelScope.launch {
            userRepository.addSearchTerm(searchTerm)
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            userRepository.clearSearchHistory()
        }
    }
}
