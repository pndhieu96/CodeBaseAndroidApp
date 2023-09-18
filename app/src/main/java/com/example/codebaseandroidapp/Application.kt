package com.example.codebaseandroidapp

import android.app.Application
import com.example.codebaseandroidapp.di.KoinApplicationModule
import com.example.codebaseandroidapp.utils.DebugTree
import com.example.codebaseandroidapp.utils.NonDebugTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class Application : Application() {
    companion object {
        private lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        if (!BuildConfig.DEBUG) {
            Timber.plant(NonDebugTree())
        } else {
            Timber.plant(DebugTree())
        }
        instance = this
        startKoin{
            androidLogger()
            androidContext(this@Application)
            modules(listOf(
                KoinApplicationModule.retrofitModule,
                KoinApplicationModule.apiModule,
                KoinApplicationModule.databaseModule,
                KoinApplicationModule.repositoryModule,
                KoinApplicationModule.viewModelModule,
                KoinApplicationModule.adapterModule,
                KoinApplicationModule.diffCallbackModule
            ))
        }
    }
}