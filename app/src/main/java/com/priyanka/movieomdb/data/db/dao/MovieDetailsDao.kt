package com.priyanka.movieomdb.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.priyanka.movieomdb.data.response.MovieDetailsResponse

@Dao
interface MovieDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(details: MovieDetailsResponse)

    @Update
    fun update(details: MovieDetailsResponse)

    @Query("SELECT * FROM movies_details WHERE imdbID = :id")
    fun getDetailsByID(id: String): LiveData<MovieDetailsResponse>


}