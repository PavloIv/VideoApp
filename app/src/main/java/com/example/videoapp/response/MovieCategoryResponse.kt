package com.example.videoapp.response

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

data class MovieCategoryResponse(
    val title: String,
    val moviesListResponse: Flow<PagingData<MoviesListResponse.Result>>
)
