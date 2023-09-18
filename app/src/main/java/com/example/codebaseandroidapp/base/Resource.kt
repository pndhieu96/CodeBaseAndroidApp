package com.example.codebaseandroidapp.base

import com.example.codebaseandroidapp.model.ApiError
import com.example.codebaseandroidapp.model.ResourceStatus
import java.util.concurrent.atomic.AtomicBoolean

sealed class Resource<T>(
    val data: T? = null,
    val error: ApiError? = null,
    val status: ResourceStatus
) {
    var hasBeenHandled = AtomicBoolean(false)

    class Success<T>(data: T?) : Resource<T>(data = data, status = ResourceStatus.SUCCESS)

    class Error<T>(error: ApiError) : Resource<T>(error = error, status = ResourceStatus.ERROR)

    class Loading<T>() : Resource<T>(status = ResourceStatus.LOADING)

    fun getContentIfNotHandled() : T?{
        return if(hasBeenHandled.getAndSet(true).not()) {
            data
        } else {
            null
        }
    }

    fun getErrorIfNotHandled() : ApiError?{
        return if(hasBeenHandled.getAndSet(true).not())
            error
        else
            null
    }

    fun isLoadingIfNotHandled() : Boolean? {
        return if(hasBeenHandled.getAndSet(true).not()) {
            status == ResourceStatus.LOADING
        } else {
            null
        }
    }
}