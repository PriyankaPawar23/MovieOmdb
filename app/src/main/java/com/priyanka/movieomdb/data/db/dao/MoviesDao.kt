package com.priyanka.movieomdb.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.priyanka.movieomdb.data.response.Search

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: Search)

    @Update
    fun update(list: Search)

    @Query("SELECT * FROM movies_list WHERE title LIKE '%' || :query || '%'")
    fun getListByQuery(query: String) : LiveData<List<Search>>


}