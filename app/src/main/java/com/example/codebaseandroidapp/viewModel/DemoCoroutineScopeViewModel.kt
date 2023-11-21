package com.example.codebaseandroidapp.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.codebaseandroidapp.model.*
import com.example.codebaseandroidapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class DemoCoroutineScopeViewModel @Inject constructor() : ViewModel() {
    private var job: Job? = null
    fun start() {
        job = viewModelScope.launch {
            Log.d("DemoCoScopeVM", "getting data...")
            delay(2000)
            Log.d("DemoCoScopeVM", "got the data")
        }
        job?.invokeOnCompletion {
            Log.d("DemoCoScopeVM", "Job finish")
        }
    }

    fun stop() {
        //Nếu dùng lệnh CoroutineScope.cancel() sẽ không tạo lại 1 coroutine cho CoroutineScope đó nữa
        //viewModelScope.cancel()

        job?.cancel()
    }
}
