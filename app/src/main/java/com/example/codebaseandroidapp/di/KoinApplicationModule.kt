package com.example.codebaseandroidapp.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.dsl.module

class KoinApplicationModule {
    companion object {
        val retrofitModule = module {
            fun provideGson(): Gson {
                return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
            }
        }
    }
}