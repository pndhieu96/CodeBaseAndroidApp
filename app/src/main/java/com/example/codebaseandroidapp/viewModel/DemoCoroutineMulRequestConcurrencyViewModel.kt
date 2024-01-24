package com.example.codebaseandroidapp.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.codebaseandroidapp.base.BaseViewModel
import com.example.codebaseandroidapp.model.*
import com.example.codebaseandroidapp.repository.FakeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DemoCoroutineMulRequestConcurrencyViewModel
    @Inject constructor(private val repository: FakeRepository) : BaseViewModel<String>() {

    public fun start(index: Int, isThrowException: Boolean) {
        if(job != null && job!!.isActive) {
            return
        }
        data.postValue(Resource.Loading<String>())
        job = viewModelScope.launch {
            launch {
                repository.requestWithIndexLaunch(1, isThrowException)
                Log.d("MulRequestConcurrency", "Done1")
                repository.requestWithIndexLaunch(2, isThrowException)
                Log.d("MulRequestConcurrency", "Done2")
            }

            var deferred = async {
                repository.requestWithIndexLaunch(4, isThrowException)
            }
            Log.d("MulRequestConcurrency", deferred.await().data.toString())

            launch {
                repository.requestWithIndexLaunch(3, isThrowException)
                Log.d("MulRequestConcurrency", "Done3")
            }

            withContext(Dispatchers.IO) {
                repository.requestWithIndexLaunch(5, isThrowException)
                Log.d("MulRequestConcurrency", "Done5")
            }
            Log.d("MulRequestConcurrency", "Done")
            data.postValue(Resource.Success<String>("Done"))
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.requestWithIndexLaunch(6, isThrowException)
                Log.d("MulRequestConcurrency", "Done6")
            }
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
