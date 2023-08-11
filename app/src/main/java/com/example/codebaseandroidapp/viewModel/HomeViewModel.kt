package com.example.codebaseandroidapp.viewModel

import androidx.lifecycle.*
import com.example.codebaseandroidapp.model.MoviesWithGenre
import com.example.codebaseandroidapp.model.Resource
import com.example.codebaseandroidapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val movieRepository: MovieRepository
): ViewModel() {
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
