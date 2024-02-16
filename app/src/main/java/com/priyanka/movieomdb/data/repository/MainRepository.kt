package com.priyanka.movieomdb.data.repository

import com.priyanka.movieomdb.data.network.RetrofitService

class MainRepository constructor(private val retrofitService: RetrofitService) {
    fun getSearchMovies(apikey: String, searchString: String, page: Int) =
        retrofitService.getSearchMovies(apikey, searchString, page)

    fun getMovieDetails(apikey: String, id: String) =
        retrofitService.getMovieDetails(id, apikey)
}