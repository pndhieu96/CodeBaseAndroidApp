package com.example.codebaseandroidapp.base

import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ViewModelHelper<DATA : BaseDataPostAPI, RESPONSE : Any> {
    suspend fun getRequest(dataPostAPI: DATA): Response<RESPONSE>
    fun callAPI(dataPostAPI: DATA)
    fun callAPIDelay(dataPostAPI: DATA, delayTime: Long = 0L)
    fun callAPISearch(dataPostAPI: DATA, delayTime: Long = 0L)
    fun callAPIFlatMapMerge(dataPostAPI: DATA, delayTime: Long = 0L)
    fun callAPIFlatMapConcat(dataPostAPI: DATA, delayTime: Long = 0L)
    fun callAPIFlatMapLatest(dataPostAPI: DATA, delayTime: Long = 0L)
    fun getRequestAPI(dataPostAPI: DATA): Flow<NetworkResult<RESPONSE, DATA>>
    fun getFlowRequestAPI(dataPostAPI: DATA): Flow<NetworkResult<RESPONSE, DATA>>
}