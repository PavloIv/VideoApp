package com.example.videoapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.videoapp.repository.ApiRepository
import com.example.videoapp.response.MoviesListResponse
import retrofit2.HttpException

class MovieCategoryPagingSource(
    private val repository: ApiRepository,
    private val category: String
) : PagingSource<Int, MoviesListResponse.Result>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesListResponse.Result> {
        return try {
            val currentPage = params.key ?: 1
            val response = repository.getCategoryMoviesList(currentPage, category)
            val data = response.body()!!.results
            val responseData = mutableListOf<MoviesListResponse.Result>()
            responseData.addAll(data)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MoviesListResponse.Result>): Int? {
        return null
    }
}