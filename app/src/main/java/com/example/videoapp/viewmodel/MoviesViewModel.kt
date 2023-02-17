package com.example.videoapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.videoapp.paging.MoviesPagingSource
import com.example.videoapp.paging.MoviesSearchPagingSource
import com.example.videoapp.repository.ApiRepository
import com.example.videoapp.response.MovieDetailsResponse
import com.example.videoapp.response.MoviesListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    val loading = MutableLiveData<Boolean>()

    val moviesList = Pager(PagingConfig(1)) {
        MoviesPagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    fun movieSearchList(query: String) = Pager(PagingConfig(1)){
        MoviesSearchPagingSource(repository,query)
    }.flow.cachedIn(viewModelScope)


    //Api
    val detailsMovie = MutableLiveData<MovieDetailsResponse>()


    fun loadDetailsMovie(id: Int) = viewModelScope.launch {
        loading.postValue(true)
        val response = repository.getMovieDetails(id)
        if (response.isSuccessful) {
            detailsMovie.postValue(response.body())
        }
        loading.postValue(false)
    }

}