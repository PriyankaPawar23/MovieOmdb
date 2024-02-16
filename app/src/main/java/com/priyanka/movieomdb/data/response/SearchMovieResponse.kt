package com.priyanka.movieomdb.data.response

data class SearchMovieResponse(
    val Response: String,
    val Search: List<Search>,
    val totalResults: String
)