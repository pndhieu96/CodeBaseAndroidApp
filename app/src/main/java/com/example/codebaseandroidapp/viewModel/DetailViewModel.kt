package com.example.codebaseandroidapp.viewModel

import androidx.lifecycle.*
import com.example.codebaseandroidapp.model.*
import com.example.codebaseandroidapp.repository.MovieRepository
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class DetailViewModel constructor(
    val movieRepository: MovieRepository
) : ViewModel() {

    private val _detailInfo = MutableLiveData<Resource<Detail>>()
    val detailInfo: LiveData<Resource<Detail>>
        get() = _detailInfo

    private val _relativeMovies = MutableLiveData<Resource<Movies>>()
    val relativeMovies: LiveData<Resource<Movies>>
        get() = _relativeMovies

    fun getDetail(id: String) {
        viewModelScope.launch {
            val resource = movieRepository.fetchDetail(id)
            _detailInfo.value = resource
        }
    }

    fun getRelativeMovie(id: String) {
        viewModelScope.launch {
            try {
                val movies = movieRepository.fetchRelativeMovies(id)
                _relativeMovies.value = movies
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addMyList(id: LoveMovieId) {
        viewModelScope.launch {
            try {
                movieRepository.AddToMyList(id)
                _detailInfo.value = _detailInfo.value?.apply {
                    this.data?.is_love = true
                    this.hasBeenHandled = AtomicBoolean(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun removeFromMyList(id: Int) {
        viewModelScope.launch {
            try {
                movieRepository.removeFromMyList(id)
                _detailInfo.value = _detailInfo.value?.apply {
                    this.data?.is_love = false
                    this.hasBeenHandled = AtomicBoolean(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
