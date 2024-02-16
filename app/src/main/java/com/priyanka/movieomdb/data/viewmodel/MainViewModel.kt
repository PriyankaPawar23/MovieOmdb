package com.priyanka.movieomdb.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.priyanka.movieomdb.data.repository.MainRepository
import com.priyanka.movieomdb.data.response.MovieDetailsResponse
import com.priyanka.movieomdb.data.response.SearchMovieResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel constructor(private val repository: MainRepository) : ViewModel() {

    val movieList = MutableLiveData<SearchMovieResponse>()
    val errorMessage = MutableLiveData<String>()

    val movieDetails = MutableLiveData<MovieDetailsResponse>()
    val errorMessageDetails = MutableLiveData<String>()

    fun getSearchMovies(apikey: String, searchString: String, page: Int) {
        val response = repository.getSearchMovies(apikey, searchString, page)
        response.enqueue(object : Callback<SearchMovieResponse> {
            override fun onResponse(
                call: Call<SearchMovieResponse>,
                response: Response<SearchMovieResponse>
            ) {
                movieList.postValue(response.body())
            }

            override fun onFailure(call: Call<SearchMovieResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getMovieDetails(apikey: String, id: String) {
        val response = repository.getMovieDetails(apikey, id)
        response.enqueue(object : Callback<MovieDetailsResponse> {
            override fun onResponse(
                call: Call<MovieDetailsResponse>,
                response: Response<MovieDetailsResponse>
            ) {
                movieDetails.postValue(response.body())
            }

            override fun onFailure(call: Call<MovieDetailsResponse>, t: Throwable) {
                errorMessageDetails.postValue(t.message)
            }
        })
    }

}