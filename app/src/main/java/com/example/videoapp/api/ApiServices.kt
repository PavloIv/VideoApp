package com.example.videoapp.api

import com.example.videoapp.response.MovieDetailsResponse
import com.example.videoapp.response.MoviesListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    //    https://api.themoviedb.org/3/movie/550?api_key=***
    //    https://api.themoviedb.org/3/movie/popular?api_key=***
    //    https://api.themoviedb.org/3/
    //    https://api.themoviedb.org/3/discover/movie?api_key=7c9dd50606a07df965d51fc9621e1448&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_genres=18&with_watch_monetization_types=flatrate

    //    id:28,Action          //    id:36,History
    //    id:12,Adventure       //    id:27,Horror
    //    id:16,Animation       //    id:10402,Music
    //    id:35,Comedy          //    id:9648,Mystery
    //    id:80,Crime           //    id:10749,Romance
    //    id:99,Documentary     //    id:878,Science Fiction
    //    id:18,Drama           //    id:10770,TV Movie
    //    id:10751,Family       //    id:53,Thriller
    //    id:14,Fantasy         //    id:10752,War
    //    id:37,Western

    @GET("movie/popular")
    suspend fun getPopularMoviesList(@Query("page") page: Int): Response<MoviesListResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") id: Int): Response<MovieDetailsResponse>

    @GET("search/movie")
    suspend fun getSearchMovie(@Query("query") query: String, @Query("page") page: Int): Response<MoviesListResponse>

    @GET("/3/discover/movie")
    suspend fun  getCategoryMovie(@Query("page") page: Int,@Query("with_genres") category: String): Response<MoviesListResponse>

}