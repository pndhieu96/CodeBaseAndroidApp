package com.example.codebaseandroidapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.codebaseandroidapp.database.AppDatabase
import com.example.codebaseandroidapp.database.Dao.GenreDao
import com.example.codebaseandroidapp.di.AppModule
import com.example.codebaseandroidapp.model.*
import com.example.codebaseandroidapp.network.MoviePagingSource
import com.example.codebaseandroidapp.network.NetWorkService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val database: AppDatabase,
    private val netWorkService: NetWorkService,
    @AppModule.IoDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    val genres: LiveData<List<Genre>> = database.genreDao().getGenres()

    suspend fun fetchGenres() : Genres {
        val respose = netWorkService.getGenres()
        database.genreDao().insertAll(respose.genres)
        return respose
    }

    suspend fun fetchMoviesWithGenre(id: String) : Movies {
        val respose = netWorkService.getMoviesWithGenre(id)
        return respose
    }

    suspend fun fetchDetailOfMovie(id: String) : Detail {
        val respose = netWorkService.getDetail(id)
        return respose
    }

    suspend fun getRelativeMovies(id: String) : Movies {
        val respose = netWorkService.getRelativeMovie(id)
        return respose
    }

    suspend fun AddToMyList(id: LoveMovieId) : Long {
        val respose = database.movieDao().insert(id)
        return respose
    }

    suspend fun getMyList() : List<LoveMovieId> {
        val response = database.movieDao().getMoviesID()
        return response
    }

    suspend fun removeFromMyList(id: Int) : Int {
        val respose = database.movieDao().delete(id)
        return respose
    }

    fun searchMovies(query: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = false),
            pagingSourceFactory = { MoviePagingSource(netWorkService, query) }
        ).flow
    }
}

public const val ITEMS_PER_PAGE = 20