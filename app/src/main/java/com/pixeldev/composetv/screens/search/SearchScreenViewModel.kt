/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pixeldev.composetv.screens.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.pixeldev.composetv.data.repository.SearchRepository
import com.pixeldev.composetv.models.Search
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    // Paging search results
    private var _multiSearch = mutableStateOf<Flow<PagingData<Search>>>(emptyFlow())
    val multiSearchState: State<Flow<PagingData<Search>>> = _multiSearch

    var searchParam = mutableStateOf("")

    init {
        // Default search
       /* searchParam.value = ""
        searchRemoteMovie(includeAdult = true)*/
    }

    fun query(queryString: String) {
        searchParam.value = queryString
        searchRemoteMovie(includeAdult = false)
    }

    private fun searchRemoteMovie(includeAdult: Boolean) {
        viewModelScope.launch {
            if (searchParam.value.isNotEmpty()) {
                _multiSearch.value = searchRepository.multiSearch(
                    query = searchParam.value,
                    includeAdult = includeAdult
                ).map { pagingData ->
                    pagingData.filter { search ->
                        (search.title != null || search.originalTitle != null)
                    }
                }.cachedIn(viewModelScope)
            }
        }
    }
}

sealed interface SearchState {
    data object Searching : SearchState
    data class Done(val movies: PagingData<Search>) : SearchState
}

