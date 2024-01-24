package com.example.codebaseandroidapp.viewModel

import androidx.lifecycle.*
import com.example.codebaseandroidapp.base.BaseViewModel
import com.example.codebaseandroidapp.model.*
import com.example.codebaseandroidapp.repository.FakeRepository
import com.example.codebaseandroidapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class DemoCoroutineSingleRequestViewModel
    @Inject constructor(private val repository: FakeRepository) : BaseViewModel<String>() {

    public fun start(index: Int, isThrowException: Boolean) {
        if(job != null && job!!.isActive) {
            return
        }
        data.postValue(Resource.Loading<String>())
        job = viewModelScope.launch {
            val deferred = async {
                repository.requestWithIndex(index, isThrowException)
            }
            val result = deferred.await()
            data.postValue(result)
        }
        job?.invokeOnCompletion {
            if(it != null) {
                data.postValue(Resource.Success<String>("Job finish ${it?.message ?: ""}"))
            }
        }
    }

    public fun cancel() {
        job?.cancel()
    }
}
