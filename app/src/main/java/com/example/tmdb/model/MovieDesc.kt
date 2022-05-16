package com.example.tmdb.model

data class MovieDesc(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val vote_average: String,
    val release_date: String,
    val vote_count: String,
    val genres: List<GenreSingle>
)

data class GenreSingle(val id: Int, val name: String)