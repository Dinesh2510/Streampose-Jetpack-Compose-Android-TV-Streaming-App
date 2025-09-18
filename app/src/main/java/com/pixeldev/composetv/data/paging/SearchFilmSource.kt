package com.pixeldev.composetv.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pixeldev.composetv.data.remote.ApiService
import com.pixeldev.composetv.models.Search
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException
import kotlin.collections.filterNotNull

class SearchFilmSource(
    private val api: ApiService,
    private val searchParams: String,
    private val includeAdult: Boolean
) : PagingSource<Int, Search>() {

    override fun getRefreshKey(state: PagingState<Int, Search>): Int? =
        state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Search> {
        return try {
            val nextPage = params.key ?: 1
            delay(3000L) // optional, just simulating slow network

            val searchMovies = api.multiSearch(
                page = nextPage,
                searchParams = searchParams,
                includeAdult = includeAdult
            )

            // Clean results: convert nullable list into non-nullable list
            val movies: List<Search> = (searchMovies.results
                .filterNotNull()     // remove nulls inside list
                    )        // handle null list

            LoadResult.Page(
                data = movies,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (movies.isEmpty()) null else (searchMovies.page ?: nextPage) + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}
