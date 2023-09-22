package com.example.codebaseandroidapp.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.codebaseandroidapp.base.BaseDataPostAPI
import com.example.codebaseandroidapp.base.NetworkResult
import com.example.codebaseandroidapp.base.apiCall
import com.example.codebaseandroidapp.base.getError
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Response

fun <DATA : BaseDataPostAPI, RESPONSE : Any> Flow<NetworkResult<RESPONSE, DATA>>.safeStart(start: suspend FlowCollector<NetworkResult<RESPONSE, DATA>>.() -> Unit)
        : Flow<NetworkResult<RESPONSE, DATA>> = onStart {
    if (!currentCoroutineContext().isActive) return@onStart
    start()
}

fun <DATA : BaseDataPostAPI, RESPONSE : Any> Flow<NetworkResult<RESPONSE, DATA>>.safeComplete(complete: suspend FlowCollector<NetworkResult<RESPONSE, DATA>>.() -> Unit)
        : Flow<NetworkResult<RESPONSE, DATA>> = onCompletion {
    if (!currentCoroutineContext().isActive) return@onCompletion
    complete()
}

fun <DATA : BaseDataPostAPI, RESPONSE : Any> Flow<NetworkResult<RESPONSE, DATA>>.safeCatch(onCatch: suspend FlowCollector<NetworkResult<RESPONSE, DATA>>.(cause: Throwable) -> Unit)
        : Flow<NetworkResult<RESPONSE, DATA>> = catch {
    if (!currentCoroutineContext().isActive) return@catch
    onCatch(it)
}

suspend inline fun <DATA : BaseDataPostAPI, RESPONSE : Any> Flow<NetworkResult<RESPONSE, DATA>>.safeCollectVM(
    crossinline onCollect: suspend (value: NetworkResult<RESPONSE, DATA>) -> Unit
)
        : Unit = collect {
    if (!currentCoroutineContext().isActive) return@collect
    onCollect(it)
}

suspend inline fun <DATA> Flow<DATA>.safeCollect(crossinline onCollect: suspend (value: DATA) -> Unit)
        : Unit = collect {
    if (!currentCoroutineContext().isActive) return@collect
    onCollect(it)
}

@Suppress("unused")
inline fun <T> Flow<T>.collectIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend (value: T) -> Unit
): Job = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(state = minActiveState) { safeCollect { action(it) } }
}

inline fun <T> Flow<T>.collectIn(
    viewModelScope: CoroutineScope,
    crossinline action: suspend (value: T) -> Unit
): Job = viewModelScope.launch {
    safeCollect { action(it) }
}

@Suppress("unused")
inline fun <T> Flow<T>.collectInViewLifecycle(
    fragment: Fragment,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend (value: T) -> Unit
): Job = collectIn(
    owner = fragment.viewLifecycleOwner,
    minActiveState = minActiveState,
    action = action
)

fun <DATA : BaseDataPostAPI, RESPONSE : Any> getRequestAPI(
    dataPostAPI: DATA,
    request: suspend () -> Response<RESPONSE>
): Flow<NetworkResult<RESPONSE, DATA>> {
    return getFlowRequestAPI(dataPostAPI = dataPostAPI, request = request)
        .safeStart {
            emit(NetworkResult.Loading(dataPostAPI = dataPostAPI))
            delay(dataPostAPI.delayTime)
        }
        .safeCatch { e ->
            emit(e.getError(dataPostAPI = dataPostAPI))
        }
}

fun <DATA : BaseDataPostAPI, RESPONSE : Any> getFlowRequestAPI(
    dataPostAPI: DATA,
    request: suspend () -> Response<RESPONSE>
): Flow<NetworkResult<RESPONSE, DATA>> {
    return flow {
        if (currentCoroutineContext().isActive) {
            emit(apiCall(dataPostAPI) {
                request.invoke()
            })
        }
    }
}

fun <RESPONSE, DATA : BaseDataPostAPI> convertFlow(
    dataPostAPI: DATA,
    block: (suspend () -> Response<RESPONSE>)
): Flow<NetworkResult<RESPONSE, DATA>> {
    return flow {
        if (currentCoroutineContext().isActive) {
            emit(apiCall(dataPostAPI) {
                block.invoke()
            })
        }
    }
}