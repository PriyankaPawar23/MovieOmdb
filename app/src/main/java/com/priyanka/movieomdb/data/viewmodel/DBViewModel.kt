package com.priyanka.movieomdb.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.priyanka.movieomdb.data.repository.DBRepository
import com.priyanka.movieomdb.data.response.MovieDetailsResponse
import com.priyanka.movieomdb.data.response.Search

class DBViewModel(
    private val repository: DBRepository
) : ViewModel() {

    suspend fun insertMovies(movies: Search) = repository.insertMovies(movies)

    suspend fun insertMovieDetails(details: MovieDetailsResponse) =
        repository.insertMovieDetails(details)

    fun getAllListByQuery(query: String): LiveData<List<Search>> =
        repository.getAllListByQuery(query)

    fun getMovieDetails(id: String): LiveData<MovieDetailsResponse> = repository.getMovieDetails(id)
}