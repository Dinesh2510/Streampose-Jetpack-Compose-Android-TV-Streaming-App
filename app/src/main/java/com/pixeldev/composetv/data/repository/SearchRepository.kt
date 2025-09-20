package com.pixeldev.composetv.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pixeldev.composetv.data.paging.SearchFilmSource
import com.pixeldev.composetv.data.remote.ApiService
import com.pixeldev.composetv.models.Search


import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val api: ApiService
) {
    fun multiSearch(query: String, includeAdult: Boolean): Flow<PagingData<Search>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = {
                SearchFilmSource(api = api, searchParams = query, includeAdult)
            }
        ).flow
    }
}
