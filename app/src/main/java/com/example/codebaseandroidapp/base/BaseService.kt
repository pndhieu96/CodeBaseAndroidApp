package com.example.codebaseandroidapp.base

import com.example.codebaseandroidapp.model.ApiError
import com.example.codebaseandroidapp.utils.ConstantUtils
import com.example.codebaseandroidapp.utils.Utils.Companion.checkNull
import com.example.codebaseandroidapp.utils.Utils.Companion.getJsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

suspend fun <RESPONSE, DATA : BaseDataPostAPI> apiCall(
    dataPostAPI: DATA,
    request: (suspend () -> Response<RESPONSE>)
): NetworkResult<RESPONSE, DATA> {
    return withContext(Dispatchers.IO) {
        try {
            val response = request.invoke()
            response.safeResponse(dataPostAPI = dataPostAPI)
        } catch (e: Throwable) {
            e.getError(dataPostAPI = dataPostAPI)
        }
    }
}

fun <RESPONSE, DATA : BaseDataPostAPI> Response<RESPONSE>.safeResponse(dataPostAPI: DATA): NetworkResult<RESPONSE, DATA> {
    val response = this
    return if (response.isSuccessful && response.code() == 200) {
        NetworkResult.Success(
            data = response.body(),
            code = response.code(),
            dataPostAPI = dataPostAPI,
            headers = response.headers()
        )
    } else {
        try {
            val errorBodyString = response.errorBody()?.string() ?: ""
            val errorModel = getJsonParser().decodeFromString<ApiError>(errorBodyString)
            var errorMessage = if (errorModel.message.checkNull().isEmpty()) {
                if (errorModel.messageError.message.checkNull().isEmpty()) {
                    getMessageErrorService()
                } else {
                    errorModel.messageError.message
                }
            } else {
                errorModel.message
            }

            NetworkResult.Error(
                errorModel = errorModel,
                errorType = NetworkResult.ApiErrorType.NULL,
                dataPostAPI = dataPostAPI,
                message = errorMessage,
                code = response.code()
            )
        } catch (e: Exception) {
            NetworkResult.Error(
                errorType = NetworkResult.ApiErrorType.NULL,
                dataPostAPI = dataPostAPI,
                message = getMessageErrorService(),
                code = response.code()
            )
        }
    }
}

fun <RESPONSE, DATA : BaseDataPostAPI> Throwable.getError(dataPostAPI: DATA): NetworkResult<RESPONSE, DATA> {
    return when (val error = this) {
        is HttpException -> NetworkResult.Error(
            errorType = NetworkResult.ApiErrorType.HTTP_ERROR,
            dataPostAPI = dataPostAPI,
            message = "${error.message} | code ${error.response()?.code()}",
            code = error.code()
        )
        is SocketTimeoutException -> NetworkResult.Error(
            code = ConstantUtils.CodeAPI.CodeTimeOut,
            errorType = NetworkResult.ApiErrorType.SOCKET_TIMEOUT,
            dataPostAPI = dataPostAPI,
            message = getMessageErrorService()
        )
        is IOException -> NetworkResult.Error(
            errorType = NetworkResult.ApiErrorType.NETWORK_ERROR,
            dataPostAPI = dataPostAPI,
            message = getMessageErrorService(isNetwork = true)
        )
        else -> NetworkResult.Error(
            errorType = NetworkResult.ApiErrorType.UNKNOWN_ERROR,
            dataPostAPI = dataPostAPI,
            message = getMessageErrorService()
        )
    }
}

fun getMessageErrorService(isNetwork: Boolean = false): String {
    return "Có lỗi xảy ra.\n";
}