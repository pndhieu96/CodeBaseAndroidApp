package com.example.codebaseandroidapp.repository

import android.util.Log
import com.example.codebaseandroidapp.di.AppModule
import com.example.codebaseandroidapp.model.ApiError
import com.example.codebaseandroidapp.model.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FakeRepository
    @Inject constructor(
        @AppModule.IoDispatcher
        private val dispatcher: CoroutineDispatcher
    ) {
    suspend fun requestWithIndex(index: Int, isThrowException: Boolean = false): Resource<String> =
    withContext(dispatcher) {
        try {
            Log.d(
                "FakeRepository",
                "Request with index $index at ${System.currentTimeMillis()} on thread ${Thread.currentThread().name}"
            )
            val delayTime = index * 1000L
            delay(delayTime)
            if (isThrowException) {
                throw Exception("Exception with index $index")
            }
            Resource.Success<String>(data = "Result for $index")
        } catch (e: java.lang.Exception) {
            Resource.Error<String>(ApiError(statusMessage = e.message ?: "error"))
        }
    }

    suspend fun requestWithIndexLaunch(index: Int, isThrowException: Boolean = false): Resource<String> =
    try {
        Log.d(
            "FakeRepository",
            "Request with index $index at ${System.currentTimeMillis()} on thread ${Thread.currentThread().name}"
        )
        val delayTime = 1 * 1000L
        delay(delayTime)
        if (isThrowException) {
            throw Exception("Exception with index $index")
        }
        Resource.Success<String>(data = "Result for $index")
    } catch (e: java.lang.Exception) {
        Resource.Error<String>(ApiError(statusMessage = e.message ?: "error"))
    }
}