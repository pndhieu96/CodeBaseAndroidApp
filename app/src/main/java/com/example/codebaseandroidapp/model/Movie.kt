package com.example.codebaseandroidapp.model

import androidx.room.*
import com.example.codebaseandroidapp.utils.Converters


data class Movie(
    var id: Int,
    var adult: Boolean,
    var backdrop_path: String,
    var genre_ids: List<Int>,
    var original_language: String,
    var original_title: String,
    var overview: String,
    var popularity: Double,
    var poster_path: String,
    var release_date: String,
    var title: String,
    var video: Boolean,
    var vote_average: Double,
    var vote_count: Int,
    var is_love: Boolean
) {
    constructor(): this(
        0,
        false,
        "",
        mutableListOf(),
        "",
        "",
        "",
        0.0,
        "",
        "",
        "",
        false,
        0.0,
        0,
        false
    )
}