package com.example.codebaseandroidapp.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.example.codebaseandroidapp.model.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job

open class BaseViewModel<T: Any> : ViewModel() {
    var data = MutableLiveData<Resource<T>>()
        private set

    protected var job: Job? = null
}