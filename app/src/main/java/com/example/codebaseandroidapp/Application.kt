package com.example.codebaseandroidapp

import android.app.Application
import androidx.room.RoomDatabase
import com.example.codebaseandroidapp.database.AppDatabase
import com.example.codebaseandroidapp.network.NetWorkService
import com.example.codebaseandroidapp.repository.MovieRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : Application()