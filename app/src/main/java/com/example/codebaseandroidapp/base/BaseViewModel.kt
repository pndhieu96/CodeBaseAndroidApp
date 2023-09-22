package com.example.codebaseandroidapp.base

import androidx.lifecycle.*
import com.example.codebaseandroidapp.model.ApiError
import com.example.codebaseandroidapp.utils.Utils.Companion.checkNull
import com.example.codebaseandroidapp.utils.collectIn
import com.example.codebaseandroidapp.utils.safeCatch
import com.example.codebaseandroidapp.utils.safeComplete
import com.example.codebaseandroidapp.utils.safeStart
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.util.concurrent.atomic.AtomicBoolean

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseViewModel<DATA : BaseDataPostAPI, RESPONSE : Any> : ViewModel(),
    ViewModelHelper<DATA, RESPONSE> {

    private val _responseFlow: MutableStateFlow<NetworkResult<RESPONSE, DATA>> by lazy {
        MutableStateFlow(NetworkResult.Init())
    }

    private val _responseChannel: Channel<NetworkResult<RESPONSE, DATA>> by lazy {
        Channel()
    }

    private val responseStateFlow: StateFlow<NetworkResult<RESPONSE, DATA>> =
        _responseFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NetworkResult.Init()
        )
    private var collectType: CollectType = CollectType.CollectStateFlow
    private val responseChannel = _responseChannel.receiveAsFlow()
    private val responseLiveData = _responseFlow.asLiveData(viewModelScope.coroutineContext)

    fun getResLiveData() = responseLiveData

    override fun callAPI(dataPostAPI: DATA) {
        getSafeRequestAPI(dataPostAPI = dataPostAPI)
            .collectRequestApi()
    }

    override fun callAPIDelay(dataPostAPI: DATA, delayTime: Long) {
        callAPI(dataPostAPI.apply { this.delayTime = delayTime })
    }

    private val dataSearchPostAPI: MutableStateFlow<DATA?> by lazy { MutableStateFlow(null) }
    private val atomicInitSearch by lazy { AtomicBoolean(false) }

    private fun initApiSearch() {
        if (!atomicInitSearch.getAndSet(true)) {
            dataSearchPostAPI
                .filterNotNull()
                .debounce(300L)
                .distinctUntilChanged { old, new ->
                    old.keyword.checkNull() == new.keyword.checkNull()
                }
                .buffer()
                .flatMapLatest { data ->
                    getSafeRequestAPI(dataPostAPI = data)
                }
                .flowOn(Dispatchers.Default)
                .safeCatch { e ->
                    emit(e.getError(dataPostAPI = dataSearchPostAPI.value!!))
                }
                .collectRequestApi()
        }
    }

    override fun callAPISearch(dataPostAPI: DATA, delayTime: Long) {
        initApiSearch()
        dataSearchPostAPI.update { dataPostAPI.apply { this.delayTime = delayTime } }
    }

    private val dataFlatMapMergePostAPI: MutableStateFlow<DATA?> by lazy { MutableStateFlow(null) }
    private val atomicInitApiFlatMapMerge by lazy { AtomicBoolean(false) }
    private fun initApiFlatMapMerge() {
        if (atomicInitApiFlatMapMerge.getAndSet(true).not()) {
            dataFlatMapMergePostAPI
                .filterNotNull()
                .buffer()
                .flatMapMerge { data ->
                    getSafeRequestAPI(dataPostAPI = data)
                }.collectRequestApi()
        }
    }

    override fun callAPIFlatMapMerge(dataPostAPI: DATA, delayTime: Long) {
        initApiFlatMapMerge()
        dataFlatMapMergePostAPI.update { dataPostAPI.apply { this.delayTime = delayTime } }
    }

    private val dataFlatMapConcatPostAPI: MutableStateFlow<DATA?> by lazy { MutableStateFlow(null) }
    private val atomicInitApiFlatMapConcat by lazy { AtomicBoolean(false) }
    private fun initApiFlatMapConcat() {
        if (atomicInitApiFlatMapConcat.getAndSet(true).not()) {
            dataFlatMapConcatPostAPI
                .filterNotNull()
                .buffer()
                .flatMapConcat { data ->
                    getSafeRequestAPI(dataPostAPI = data)
                }.collectRequestApi()
        }
    }

    override fun callAPIFlatMapConcat(dataPostAPI: DATA, delayTime: Long) {
        initApiFlatMapConcat()
        dataFlatMapConcatPostAPI.update { dataPostAPI.apply { this.delayTime = delayTime } }
    }

    private val dataFlatMapLatestPostAPI: MutableStateFlow<DATA?> by lazy { MutableStateFlow(null) }
    private val atomicInitApiFlatMapLatest by lazy { AtomicBoolean(false) }
    private fun initApiFlatMapLatest() {
        if (atomicInitApiFlatMapLatest.getAndSet(true).not()) {
            dataFlatMapLatestPostAPI
                .filterNotNull()
                .buffer()
                .flatMapLatest { data ->
                    getSafeRequestAPI(dataPostAPI = data)
                }.collectRequestApi()
        }
    }

    override fun callAPIFlatMapLatest(dataPostAPI: DATA, delayTime: Long) {
        initApiFlatMapLatest()
        dataFlatMapLatestPostAPI.update { dataPostAPI.apply { this.delayTime = delayTime } }
    }

    private fun Flow<NetworkResult<RESPONSE, DATA>>.collectRequestApi() {
        collectIn(viewModelScope = viewModelScope) { response ->
            updateResponse(data = response)
        }
    }

    private fun updateResponse(data: NetworkResult<RESPONSE, DATA>) {
        when (collectType) {
            CollectType.CollectStateFlow -> {
                _responseFlow.update { data }
            }
            CollectType.CollectChannel -> {
                viewModelScope.launch {
                    _responseChannel.send(data)
                }
            }
        }
    }

    override fun getRequestAPI(
        dataPostAPI: DATA
    ): Flow<NetworkResult<RESPONSE, DATA>> {
        return getFlowRequestAPI(dataPostAPI = dataPostAPI)
    }

    private fun getSafeRequestAPI(dataPostAPI: DATA): Flow<NetworkResult<RESPONSE, DATA>> {
        return getRequestAPI(dataPostAPI = dataPostAPI)
            .safeStart {
                emit(NetworkResult.Loading(dataPostAPI = dataPostAPI))
                delay(dataPostAPI.delayTime)
            }.safeComplete {
                emit(NetworkResult.Complete())
            }
            .safeCatch { e ->
                emit(e.getError(dataPostAPI = dataPostAPI))
            }
    }

    protected fun <RESPONSE, DATA : BaseDataPostAPI> convertFlow(
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

    override fun getFlowRequestAPI(
        dataPostAPI: DATA
    ): Flow<NetworkResult<RESPONSE, DATA>> {
        return flow {
            if (currentCoroutineContext().isActive) {
                emit(apiCall(dataPostAPI) {
                    getRequest(dataPostAPI)
                })
            }
        }
    }

    fun observe(
        owner: LifecycleOwner,
        onShowLoading: (() -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
        onSuccess: ((response: RESPONSE, headers: okhttp3.Headers?, code: Int) -> Unit)? = null,
        onError: ((code: Int, errorType: Int, error: ApiError) -> Unit)? = null
    ) {
        collectType = CollectType.CollectStateFlow
        responseLiveData.removeObservers(owner)
        responseLiveData.observe(owner) {
            when (it.status) {
                NetworkResult.NetworkStatus.INIT -> {
                }
                NetworkResult.NetworkStatus.LOADING -> {
                    onShowLoading?.invoke()
                }
                NetworkResult.NetworkStatus.COMPLETE -> {
                    onComplete?.invoke()
                }
                NetworkResult.NetworkStatus.SUCCESS -> {
                    it.getConsumeResponse { response ->
                        onSuccess?.invoke(response, it.headers, it.code)
                    }
                }
                NetworkResult.NetworkStatus.ERROR -> {
                    it.getErrorConsumeResponse { errorCode, errorType, errorData ->
                        onError?.invoke(errorCode, errorType, errorData)
                    }
                }
            }
        }
    }

    fun collectStateFlow(
        owner: LifecycleOwner,
        onShowLoading: ((metaData: MetaDataModel<DATA>) -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
        onSuccess: ((response: RESPONSE, metaData: MetaDataModel<DATA>) -> Unit)? = null,
        onError: ((metaData: MetaDataModel<DATA>) -> Unit)? = null
    ) {
        collectType = CollectType.CollectStateFlow
        responseStateFlow.collectIn(
            owner = owner,
            minActiveState = Lifecycle.State.RESUMED
        ) {
            doObserver(
                data = it,
                onShowLoading = onShowLoading,
                onComplete = onComplete,
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }

    fun collectChannel(
        owner: LifecycleOwner,
        onShowLoading: ((metaData: MetaDataModel<DATA>) -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
        onSuccess: ((response: RESPONSE, metaData: MetaDataModel<DATA>) -> Unit)? = null,
        onError: ((metaData: MetaDataModel<DATA>) -> Unit)? = null
    ) {
        collectType = CollectType.CollectChannel
        responseChannel.collectIn(
            owner = owner,
            minActiveState = Lifecycle.State.RESUMED
        ) {
            doObserver(
                data = it,
                onShowLoading = onShowLoading,
                onComplete = onComplete,
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }

    fun observeV2(
        owner: LifecycleOwner,
        onShowLoading: ((metaData: MetaDataModel<DATA>) -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
        onSuccess: ((response: RESPONSE, metaData: MetaDataModel<DATA>) -> Unit)? = null,
        onError: ((metaData: MetaDataModel<DATA>) -> Unit)? = null
    ) {
        collectType = CollectType.CollectStateFlow
        responseLiveData.removeObservers(owner)
        responseLiveData.observe(owner) {
            doObserver(
                data = it,
                onShowLoading = onShowLoading,
                onComplete = onComplete,
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }

    private fun doObserver(
        data: NetworkResult<RESPONSE, DATA>,
        onShowLoading: ((metaData: MetaDataModel<DATA>) -> Unit)?,
        onComplete: (() -> Unit)?,
        onSuccess: ((response: RESPONSE, metaData: MetaDataModel<DATA>) -> Unit)?,
        onError: ((metaData: MetaDataModel<DATA>) -> Unit)?
    ) {
        when (data.status) {
            NetworkResult.NetworkStatus.INIT -> {
            }
            NetworkResult.NetworkStatus.COMPLETE -> {
                onComplete?.invoke()
            }
            NetworkResult.NetworkStatus.LOADING -> {
                onShowLoading?.invoke(data.getMetaData())
            }
            NetworkResult.NetworkStatus.SUCCESS -> {
                data.getConsumeResponse { response ->
                    onSuccess?.invoke(response, data.getMetaData())
                }
            }
            NetworkResult.NetworkStatus.ERROR -> {
                data.getErrorConsumeResponse { _, _, _ ->
                    onError?.invoke(data.getMetaData())
                }
            }
        }
    }

    data class MetaDataModel<DATA : BaseDataPostAPI>(
        val dataPostAPI: DATA? = null,
        val code: Int = 0,
        val errorType: Int = 0,
        val headers: okhttp3.Headers? = null,
        val errorData: ApiError = ApiError(),
        val timeStamp: String = ""
    )

    sealed class CollectType {
        object CollectStateFlow : CollectType()
        object CollectChannel : CollectType()
    }
}