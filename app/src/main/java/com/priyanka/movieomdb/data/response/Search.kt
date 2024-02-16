package com.priyanka.movieomdb.data.response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies_list")
data class Search(
    val Poster: String,
    val Title: String,
    val Type: String,
    val Year: String,
    @PrimaryKey(autoGenerate = false)
    val imdbID: String
)