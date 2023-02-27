package com.example.codebaseandroidapp.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.codebaseandroidapp.model.Genre
import com.example.codebaseandroidapp.model.MoviesWithGenre
import com.example.codebaseandroidapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.Exception
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
    private val _genres = MutableLiveData<String>()
    val genres: LiveData<List<Genre>> = movieRepository.genres

    private val _moviesWithGenre = MutableLiveData<List<MoviesWithGenre>>()
    val moviesWithGenre: LiveData<List<MoviesWithGenre>>
        get() = _moviesWithGenre

    private val _error = MutableLiveData<Exception?>()
    val error: LiveData<Exception?>
        get() = _error

    init {
        getGenres()
    }

    fun getGenres() {
        viewModelScope.launch {
            try {
                val mMoviesWithGenre : MutableList<MoviesWithGenre> = mutableListOf()
                val genres = movieRepository.fetchGenres()
                genres.genres.subList(0,10).forEachIndexed { index, it ->
                    val movies = movieRepository.fetchMoviesWithGenre(it.id.toString())
                    if(movies.results.size > 0) {
                        val moviesWithGenre = MoviesWithGenre(it.id, it.name, movies.results)
                        mMoviesWithGenre.add(moviesWithGenre)
                    }
                    if(index == 3) {
                        _moviesWithGenre.value = mMoviesWithGenre
                    }
                }
                _moviesWithGenre.value = mMoviesWithGenre
            } catch (e: HttpException) {
                Log.d("HomeViewModel", e.message.toString())
                _error.value = e
                clearError()
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}

class HomeViewModelFactory(
    private val repository: MovieRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = HomeViewModel(repository) as T
}
