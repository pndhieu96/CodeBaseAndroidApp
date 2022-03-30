package com.example.codebaseandroidapp.database.Dao

import androidx.room.*
import com.example.codebaseandroidapp.model.LoveMovieId
import com.example.codebaseandroidapp.model.Movie
import com.example.codebaseandroidapp.model.Movies

@Dao
interface MovieDao {
    @Query("SELECT * FROM love_movies ORDER BY serial DESC")
    suspend fun getMoviesID() : List<LoveMovieId>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(id: LoveMovieId) : Long

    @Query("DELETE FROM love_movies WHERE id = :id")
    suspend fun delete(id: Int): Int
}