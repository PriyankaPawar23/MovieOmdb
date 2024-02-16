package com.priyanka.movieomdb.data.network

import android.content.Context
import com.priyanka.movieomdb.constant.ApiConstant
import com.priyanka.movieomdb.data.response.MovieDetailsResponse
import com.priyanka.movieomdb.data.response.SearchMovieResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("/")
    fun getSearchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") searchQuery: String,
        @Query("page") pageNo: Int
    ): Call<SearchMovieResponse>


    @GET("/")
    fun getMovieDetails(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String
    ): Call<MovieDetailsResponse>

    companion object {

        private var retrofitService         : RetrofitService? = null


        fun getInstance(context: Context): RetrofitService {

            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(ApiConstant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }

}