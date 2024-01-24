package com.example.codebaseandroidapp.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.codebaseandroidapp.model.*
import com.example.codebaseandroidapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class DemoCoroutineExceptionHandlerViewModel @Inject constructor() : ViewModel() {

    private var job: Job? = null
    fun getDataWithTryCatch(){
        job = viewModelScope.launch {
            launch {
                try {
                    downloadData(1, true)
                } catch (e: java.lang.Exception) {
                    Log.d("CoExceptionHandlerVM", "Exception: ${e.message}")
                }
            }
            launch {
                try {
                    downloadData(2, false)
                } catch (e: java.lang.Exception) {
                    Log.d("CoExceptionHandlerVM", "Exception: ${e.message}")
                }
            }
            Log.d("CoExceptionHandlerVM", "Load done")
        }
        job?.invokeOnCompletion {
            Log.d("CoExceptionHandlerVM", "Job finish ${it?.message ?: ""}")
        }
    }

    fun getDataWithExceptionHandler(){
        val parentExceptionHandler = CoroutineExceptionHandler{ _, t ->
            Log.d("CoExceptionHandlerVM", "Parent exception: ${t.message}")
        }
        job = viewModelScope.launch(parentExceptionHandler) {
            supervisorScope {
                val job1ExceptionHandler = CoroutineExceptionHandler { _, t ->
                    Log.d("CoExceptionHandlerVM", "Job 1 exception: ${t.message}")
                }
                launch(job1ExceptionHandler) {
                    downloadData(1, true)
                }

                val job2ExceptionHandler = CoroutineExceptionHandler { _, t ->
                    Log.d("CoExceptionHandlerVM", "Job 2 exception: ${t.message}")
                }
                launch(job2ExceptionHandler) {
                    downloadData(2, false)
                }
            }
        }
        job?.invokeOnCompletion {
            Log.d("CoExceptionHandlerVM", "Job finish ${it?.message ?: ""}")
        }
    }

    private suspend fun downloadData(index: Int, isThrowException: Boolean = false): Int {
        Log.d("CoExceptionHandlerVM", "Downloading data for index: $index. Start at ${System.currentTimeMillis()} in thread ${Thread.currentThread()}")
        var delayTime = index * 1000L;
        delay(delayTime)
        if(isThrowException)
            throw Exception("Exception for index: $index")
        Log.d("CoExceptionHandlerVM", "Got the data for index: $index at ${System.currentTimeMillis()}")

        //Khi popBackstack() viewmodelScope bị cancel thì hàm này cũng bị cancel
        //saveToDB()

        //Khi popBackstack() viewmodelScope phài chờ hàm nay completed mới bị cancel
        saveToDBWithNonCancel()

        return index
    }

    private suspend fun saveToDB() {
        delay(2000L)
        Log.d("CoExceptionHandlerVM", "Save to the database success")
    }

    private suspend fun saveToDBWithNonCancel() = withContext(NonCancellable){
        delay(2000L)
        Log.d("CoExceptionHandlerVM", "Save to the database success")
    }
}
