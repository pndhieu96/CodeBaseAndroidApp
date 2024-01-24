package com.example.codebaseandroidapp.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.codebaseandroidapp.base.BaseViewModel
import com.example.codebaseandroidapp.model.*
import com.example.codebaseandroidapp.repository.FakeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DemoCoroutineMulRequestSequentialViewModel
    @Inject constructor(private val repository: FakeRepository) : BaseViewModel<String>() {

    public fun start(index: Int, isThrowException: Boolean) {
        if(job != null && job!!.isActive) {
            return
        }
        data.postValue(Resource.Loading<String>())
        job = viewModelScope.launch {
            repository.requestWithIndex(index, isThrowException)
            repository.requestWithIndex(index, isThrowException)
            repository.requestWithIndex(index, isThrowException)
            data.postValue(Resource.Success<String>("Done"))
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
