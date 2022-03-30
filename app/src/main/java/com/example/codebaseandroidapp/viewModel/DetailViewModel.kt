package com.example.codebaseandroidapp.viewModel

import androidx.lifecycle.*
import com.example.codebaseandroidapp.model.Detail
import com.example.codebaseandroidapp.model.LoveMovieId
import com.example.codebaseandroidapp.model.Movie
import com.example.codebaseandroidapp.model.Movies
import com.example.codebaseandroidapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    val movieRepository: MovieRepository
) : ViewModel() {

    private val _detailInfo = MutableLiveData<Detail>()
    val detailInfo: LiveData<Detail>
        get() = _detailInfo

    private val _relativeMovies = MutableLiveData<Movies>()
    val relativeMovies: LiveData<Movies>
        get() = _relativeMovies

    fun getDetail(id: String) {
        viewModelScope.launch {
            try {
                val detail = movieRepository.fetchDetailOfMovie(id)
                val myList = movieRepository.getMyList()
                if(isInMyMist(myList, detail.id)) {
                    detail.is_love = true
                }
                _detailInfo.value = detail
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getRelativeMovie(id: String) {
        viewModelScope.launch {
            try {
                val movies = movieRepository.getRelativeMovies(id)
                _relativeMovies.value = movies
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun isInMyMist(list : List<LoveMovieId>, id: Int): Boolean {
        list.forEach {
            if(it.id == id) {
                return true
            }
        }
        return false
    }

    fun addMyList(id: LoveMovieId) {
        viewModelScope.launch {
            try {
                movieRepository.AddToMyList(id)
                _detailInfo.value = _detailInfo.value?.apply {
                    this.is_love = true
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
                    this.is_love = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class DetailViewModelFactory(
    private val repository: MovieRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = DetailViewModel(repository) as T
}
