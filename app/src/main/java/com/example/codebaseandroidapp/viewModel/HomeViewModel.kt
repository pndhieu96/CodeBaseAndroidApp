package com.example.codebaseandroidapp.viewModel

import androidx.lifecycle.*
import com.example.codebaseandroidapp.model.MoviesWithGenre
import com.example.codebaseandroidapp.model.Resource
import com.example.codebaseandroidapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Hilt-3
 * @Inject constructor
 * Để chỉ cho hilt cách cung cấp đối tượng
 *
 * @HiltViewModel
 * Để chỉ cho hilt cách cung cấp đối tượng ViewModel
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    val movieRepository: MovieRepository
): ViewModel() {
    //MutableLiveData là Một biến LiveData có thể thay đổi dữ liêu mà nó swap
    private val _moviesWithGenre = MutableLiveData<Resource<List<MoviesWithGenre>>>()
    val moviesWithGenre: LiveData<Resource<List<MoviesWithGenre>>>
        get() = _moviesWithGenre

    init {
        getGenres()
    }

    fun getGenres() {
        _moviesWithGenre.value = Resource.Loading()
        viewModelScope.launch {
            val mMoviesWithGenres = movieRepository.fetchMoviesWithGenres()
            _moviesWithGenre.value = mMoviesWithGenres
        }
    }
}

//class HomeViewModelFactory(
//    private val repository: MovieRepository
//) : ViewModelProvider.NewInstanceFactory() {
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>) = HomeViewModel(repository) as T
//}
