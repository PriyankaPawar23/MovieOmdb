package com.priyanka.movieomdb.data.repository

import androidx.lifecycle.LiveData
import com.priyanka.movieomdb.data.db.AppDatabase
import com.priyanka.movieomdb.data.response.MovieDetailsResponse
import com.priyanka.movieomdb.data.response.Search

class DBRepository constructor(private val db: AppDatabase) {

    suspend fun insertMovies(movies: Search) = db.getMoviesDao().insert(movies)

    suspend fun insertMovieDetails(details: MovieDetailsResponse) =
        db.getMoviesDetailsDao().insert(details)

    fun getAllListByQuery(query: String): LiveData<List<Search>> =
        db.getMoviesDao().getListByQuery(query)

    fun getMovieDetails(id: String): LiveData<MovieDetailsResponse> =
        db.getMoviesDetailsDao().getDetailsByID(id)
}