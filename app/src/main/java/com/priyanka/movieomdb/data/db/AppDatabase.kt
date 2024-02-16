package com.priyanka.movieomdb.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.priyanka.movieomdb.data.db.dao.MovieDetailsDao
import com.priyanka.movieomdb.data.db.dao.MoviesDao
import com.priyanka.movieomdb.data.response.MovieDetailsResponse
import com.priyanka.movieomdb.data.response.Search


@Database(
    entities = [Search::class,
        MovieDetailsResponse::class],
    version = 1,
    exportSchema = true

)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMoviesDao(): MoviesDao
    abstract fun getMoviesDetailsDao(): MovieDetailsDao


    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "movies"
                ).allowMainThreadQueries()
                    .build().also {
                        instance = it
                    }
            }
        }
    }
}
