package com.example.codebaseandroidapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.codebaseandroidapp.model.Movie
import com.example.codebaseandroidapp.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class SearchViewModel constructor(
    val repository: MovieRepository
) : ViewModel() {

        var currentKey = ""
        var backToAnother = false

        var searchPagerFlow : Flow<PagingData<Movie>> = repository.searchMovies("")
            .cachedIn(viewModelScope)

        fun searchMovies(query: String) {
            currentKey = query
            searchPagerFlow = repository.searchMovies(query)
                .cachedIn(viewModelScope)
        }
}