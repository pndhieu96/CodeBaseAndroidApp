package com.example.codebaseandroidapp.viewModel

import com.example.codebaseandroidapp.base.BaseViewModel
import com.example.codebaseandroidapp.model.Detail
import com.example.codebaseandroidapp.network.DetailPostAPI
import com.example.codebaseandroidapp.network.NetWorkService
import retrofit2.Response

class GetDetailViewModel(private val netWorkService: NetWorkService) : BaseViewModel<DetailPostAPI, Detail>() {
    override suspend fun getRequest(dataPostAPI: DetailPostAPI): Response<Detail> {
        return netWorkService.getDetailBaseVM(dataPostAPI.id)
    }
}