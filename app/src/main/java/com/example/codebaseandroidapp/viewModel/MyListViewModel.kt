package com.example.codebaseandroidapp.viewModel

import androidx.lifecycle.*
import com.example.codebaseandroidapp.model.Movie
import com.example.codebaseandroidapp.model.Resource
import com.example.codebaseandroidapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyListViewModel @Inject constructor(
    val respository: MovieRepository
) : ViewModel() {
    private val _myList = MutableLiveData<Resource<List<Movie>>>()
    val myList : LiveData<Resource<List<Movie>>>
    get() = _myList

    fun getMyList() {
        viewModelScope.launch {
            val movies = respository.fetchMyMovieList()
            _myList.value = movies
        }
    }
}

//class MyListViewModelFactory(
//    private val repository: MovieRepository
//): ViewModelProvider.NewInstanceFactory() {
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return MyListViewModel(repository) as T
//    }
//}