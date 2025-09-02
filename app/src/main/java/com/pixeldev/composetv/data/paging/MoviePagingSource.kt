package com.pixeldev.composetv.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pixeldev.composetv.data.remote.ApiService


import com.pixeldev.composetv.utlis.Constants.Companion.discoverListScreen
import com.pixeldev.composetv.utlis.Constants.Companion.nowPlayingAllListScreen
import com.pixeldev.composetv.utlis.Constants.Companion.popularAllListScreen
import com.pixeldev.composetv.utlis.Constants.Companion.upcomingListScreen
import com.pixeldev.composetv.models.Movies
import kotlinx.coroutines.delay

class MoviePagingSource(private val apiService: ApiService, private val tags: String) : PagingSource<Int, Movies>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        return try {
            val nextPage = params.key ?: 1
            delay(3000L)
            val response = when(tags) {
                nowPlayingAllListScreen -> {
                    apiService.getNowPlayingMovies(page = nextPage)
                }
                discoverListScreen -> {
                    apiService.getDiscoverMovies(page = nextPage)
                }
                upcomingListScreen -> {
                    apiService.getUpcomingMovies(page = nextPage)
                }

                popularAllListScreen -> {
                    apiService.getPopularMovies(page = nextPage)

                }

                else -> {apiService.getPopularMovies(page = nextPage)}
            }
            LoadResult.Page(
                data = response.results,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (response.page >= response.totalPages) null else response.page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movies>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
