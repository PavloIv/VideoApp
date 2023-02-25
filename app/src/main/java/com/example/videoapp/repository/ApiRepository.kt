package com.example.videoapp.repository

import com.example.videoapp.api.ApiServices
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiServices: ApiServices,
) {
    suspend fun getPopularMoviesList(page: Int) = apiServices.getPopularMoviesList(page)
    suspend fun getMovieDetails(id: Int) = apiServices.getMovieDetails(id)
    suspend fun getSearchMovieList(query: String, page: Int) = apiServices.getSearchMovie(query,page)
    suspend fun getCategoryMoviesList(page: Int, category: String) = apiServices.getCategoryMovie(page,category)

}