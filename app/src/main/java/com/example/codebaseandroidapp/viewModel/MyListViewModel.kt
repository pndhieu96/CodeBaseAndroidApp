package com.example.codebaseandroidapp.viewModel

import androidx.lifecycle.*
import com.example.codebaseandroidapp.model.LoveMovieId
import com.example.codebaseandroidapp.model.Movie
import com.example.codebaseandroidapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyListViewModel @Inject constructor(
    val respository: MovieRepository
) : ViewModel() {

    private val _myListIds = MutableLiveData<List<LoveMovieId>>()
    private val _myList = MutableLiveData<List<Movie>>()
    val myList : LiveData<List<Movie>>
    get() = _myList

    fun getMyList() {
        viewModelScope.async {
            val movies = mutableListOf<Movie>()
            _myListIds.value = respository.getMyList()
            _myListIds.value?.forEach {
                val detail = respository.fetchDetailOfMovie(it.id.toString())
                movies.add(detail.transformToMovie())
            }
            _myList.value = movies
        }
    }
}

class MyListViewModelFactory(
    private val repository: MovieRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyListViewModel(repository) as T
    }
}