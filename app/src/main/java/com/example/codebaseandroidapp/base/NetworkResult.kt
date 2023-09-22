package com.example.codebaseandroidapp.base

import androidx.annotation.IntDef
import com.example.codebaseandroidapp.model.ApiError
import com.example.codebaseandroidapp.utils.Utils.Companion.getTimeStamp
import com.example.codebaseandroidapp.utils.Utils.Companion.ifIsNotNull
import com.example.codebaseandroidapp.utils.Utils.Companion.ifNotNull
import java.util.concurrent.atomic.AtomicBoolean

sealed class NetworkResult<T, DATA : BaseDataPostAPI>(
    @NetworkStatus val status: Int = 0,
    val message: String = "",
    val code: Int = 0,
    val errorType: Int = 0,
    val data: T? = null,
    val dataPostAPI: DATA? = null,
    val timeStamp: String = getTimeStamp(),
    val errorModel: ApiError = ApiError(),
    val headers: okhttp3.Headers? = null
) {

    fun copy(
        message: String = this.message,
        code: Int = this.code,
        errorType: Int = this.errorType,
        data: T? = this.data,
        dataPostAPI: DATA? = this.dataPostAPI,
        errorModel: ApiError = this.errorModel,
        headers: okhttp3.Headers? = this.headers,
        timeStamp: String = this.timeStamp
    ): NetworkResult<T, DATA> {
        val newDataPostAPI = dataPostAPI
        val newTimeStamp = timeStamp
        return when (this) {
            is Success<T, DATA> -> {
                val newData = data
                val newHeaders = headers
                Success(
                    data = newData,
                    dataPostAPI = newDataPostAPI,
                    code = code,
                    headers = newHeaders,
                    timeStamp = newTimeStamp
                )
            }
            is Error<T, DATA> -> {
                Error(
                    errorModel = errorModel,
                    dataPostAPI = newDataPostAPI,
                    code = code,
                    message = message,
                    errorType = errorType,
                    timeStamp = newTimeStamp
                )
            }
            is Init -> {
                Init(timeStamp = newTimeStamp)
            }
            is Complete -> {
                Complete(timeStamp = newTimeStamp)
            }
            is Loading -> {
                Loading(
                    dataPostAPI = newDataPostAPI,
                    timeStamp = newTimeStamp
                )
            }
        }
    }

    class Success<T, DATA : BaseDataPostAPI>(
        data: T?,
        dataPostAPI: DATA?,
        code: Int = 0, headers: okhttp3.Headers? = null,
        timeStamp: String = getTimeStamp()
    ) :
        NetworkResult<T, DATA>(
            status = NetworkStatus.SUCCESS,
            dataPostAPI = dataPostAPI,
            data = data,
            code = code,
            headers = headers,
            timeStamp = timeStamp
        )

    class Error<T, DATA : BaseDataPostAPI>(
        errorModel: ApiError = ApiError(),
        @ApiErrorType errorType: Int = ApiErrorType.NULL,
        dataPostAPI: DATA?,
        code: Int = 0,
        message: String = "",
        timeStamp: String = getTimeStamp()
    ) : NetworkResult<T, DATA>(
        status = NetworkStatus.ERROR,
        errorType = errorType,
        dataPostAPI = dataPostAPI,
        code = code,
        message = message,
        errorModel = errorModel,
        timeStamp = timeStamp
    )

    class Loading<T, DATA : BaseDataPostAPI>(
        dataPostAPI: DATA?,
        timeStamp: String = getTimeStamp()
    ) :
        NetworkResult<T, DATA>(
            status = NetworkStatus.LOADING,
            dataPostAPI = dataPostAPI,
            timeStamp = timeStamp
        )

    class Init<T, DATA : BaseDataPostAPI>(timeStamp: String = getTimeStamp()) :
        NetworkResult<T, DATA>(status = NetworkStatus.INIT, timeStamp = timeStamp)

    class Complete<T, DATA : BaseDataPostAPI>(timeStamp: String = getTimeStamp()) :
        NetworkResult<T, DATA>(status = NetworkStatus.COMPLETE, timeStamp = timeStamp)

    private val consumed = AtomicBoolean(false)

    fun consume(block: NetworkResult<T, DATA>.(T?) -> Unit) {
        if (!consumed.getAndSet(true)) {
            this.block(data)
        }
    }

    private val errorConsumed = AtomicBoolean(false)

    fun errorConsumed(block: (errorCode: Int, errorType: Int, errorData: ApiError) -> Unit) {
        if (!errorConsumed.getAndSet(true)) {
            block.invoke(code, errorType, errorModel)
        }
    }

    fun isSuccess(): Boolean {
        return status == NetworkStatus.SUCCESS
    }

    fun isLoading(): Boolean {
        return status == NetworkStatus.LOADING
    }

    fun isError(): Boolean {
        return status == NetworkStatus.ERROR
    }

    fun isInit(): Boolean {
        return status == NetworkStatus.INIT
    }

    fun isComplete(): Boolean {
        return status == NetworkStatus.COMPLETE
    }

    fun getMetaData(): BaseViewModel.MetaDataModel<DATA> {
        return BaseViewModel.MetaDataModel(
            dataPostAPI = this.dataPostAPI,
            code = this.code,
            errorType = this.errorType,
            headers = this.headers,
            errorData = this.errorModel,
            timeStamp = this.timeStamp
        )
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        NetworkStatus.SUCCESS,
        NetworkStatus.LOADING,
        NetworkStatus.ERROR,
        NetworkStatus.INIT,
        NetworkStatus.COMPLETE
    )
    internal annotation class NetworkStatus {
        companion object {
            const val SUCCESS = 0
            const val LOADING = 1
            const val ERROR = 2
            const val INIT = 3
            const val COMPLETE = 4
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        ApiErrorType.SOCKET_TIMEOUT,
        ApiErrorType.NETWORK_ERROR,
        ApiErrorType.UNKNOWN_ERROR,
        ApiErrorType.NULL,
        ApiErrorType.HTTP_ERROR
    )
    internal annotation class ApiErrorType {
        companion object {
            const val SOCKET_TIMEOUT = 0
            const val NETWORK_ERROR = 1
            const val UNKNOWN_ERROR = 2
            const val NULL = 3
            const val HTTP_ERROR = 4
        }
    }
}

fun <R, T, DATA : BaseDataPostAPI> NetworkResult<T, DATA>.getResponse(func: (T) -> R) {
    ifNotNull(this.data) {
        func(it)
    }
}

fun <R, T, DATA : BaseDataPostAPI> NetworkResult<T, DATA>.getConsumeResponse(func: (T) -> R) {
    this.consume {
        ifNotNull(this.data) {
            func(it)
        }
    }
}

fun <T, DATA : BaseDataPostAPI> NetworkResult<T, DATA>.getErrorConsumeResponse(
    func: (Int, Int, ApiError) -> Unit
) {
    this.errorConsumed { errorCode, errorType, errorData ->
        func.invoke(errorCode, errorType, errorData)
    }
}

fun <R, A, T, DATA : BaseDataPostAPI> NetworkResult<T, DATA>.getResponse(
    isNotNull: (T) -> R,
    isNull: () -> A
) {
    ifIsNotNull(this.data, {
        isNotNull(it)
    }, {
        isNull
    })
}