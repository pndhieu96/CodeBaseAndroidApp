package com.example.codebaseandroidapp.model

import androidx.room.*
import com.example.codebaseandroidapp.utils.Converters


@Entity(tableName = "love_movies")
data class LoveMovieId(
    @PrimaryKey(autoGenerate = true) var serial: Int,
    var id: Int
)