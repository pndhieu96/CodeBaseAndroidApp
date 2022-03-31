package com.example.codebaseandroidapp

import android.app.Application
import android.content.Context
import androidx.room.RoomDatabase
import com.example.codebaseandroidapp.database.AppDatabase
import com.example.codebaseandroidapp.network.NetWorkService
import com.example.codebaseandroidapp.repository.MovieRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : Application() {
    companion object {

        private lateinit var instance: Application

        var isPlayingSong = false

        fun getAppContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}