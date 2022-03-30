package com.example.codebaseandroidapp.database.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.codebaseandroidapp.model.Genre

@Dao
interface GenreDao {
    @Query("SELECT * FROM genres ORDER BY name")
    fun getGenres() : LiveData<List<Genre>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<Genre>)
}