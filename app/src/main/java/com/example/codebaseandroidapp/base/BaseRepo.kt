package com.example.codebaseandroidapp.base

import com.example.codebaseandroidapp.model.ApiError
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRepo {
    suspend fun <T> safeApiCall(apiToBeCalled: suspend () -> Response<T>) : Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<T> = apiToBeCalled()

                if (response.isSuccessful) {
                    Resource.Success(data = response.body()!!)
                } else {
                    val apiError = Gson().fromJson(response.errorBody()?.string(), ApiError::class.java)
                    Resource.Error(apiError)
                }
            } catch (e:HttpException) {
                Resource.Error(ApiError(statusMessage = e.message ?: "Something went wrong"))
            } catch (e: IOException) {
                Resource.Error(ApiError(statusMessage = "Please check your network connection"))
            } catch (e: Exception) {
                Resource.Error(ApiError(statusMessage = "Something went wrong"))
            }
        }
    }
}