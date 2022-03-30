package com.example.codebaseandroidapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.codebaseandroidapp.database.Dao.GenreDao
import com.example.codebaseandroidapp.database.Dao.MovieDao
import com.example.codebaseandroidapp.model.Genre
import com.example.codebaseandroidapp.model.LoveMovieId
import com.example.codebaseandroidapp.model.Movie
import com.example.codebaseandroidapp.utils.Converters

@Database(entities = [Genre::class, LoveMovieId::class], version = 7, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao
    abstract fun movieDao(): MovieDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(
                        context
                    ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}

public const val DATABASE_NAME = "movie-db"