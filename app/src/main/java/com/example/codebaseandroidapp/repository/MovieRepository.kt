package com.example.codebaseandroidapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.codebaseandroidapp.database.AppDatabase
import com.example.codebaseandroidapp.model.*
import com.example.codebaseandroidapp.network.MoviePagingSource
import com.example.codebaseandroidapp.network.NetWorkService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepository constructor(
    private val database: AppDatabase,
    private val netWorkService: NetWorkService,
) {
    suspend fun fetchRelativeMovies(id: String) : Resource<Movies> {
        val respose = netWorkService.getRelativeMovies(id)
        return respose
    }

    suspend fun fetchMyMovieList() : Resource<List<Movie>> {
        val movies = mutableListOf<Movie>()
        val myListIds = getMyList()
        myListIds.forEach {
            val resource = netWorkService.getDetail(it.id.toString())
            if(resource.status == ResourceStatus.SUCCESS) {
                resource.data?.let { data -> movies.add(data.transformToMovie()) }
            }
        }
        if(movies.size > 0) {
            return Resource.Success(movies)
        } else {
            return Resource.Error(ApiError())
        }
    }

    suspend fun fetchDetail(id: String) : Resource<Detail> {
        val resource = netWorkService.getDetail(id)
        if(resource.status == ResourceStatus.SUCCESS) {
            val myList = getMyList()
            resource.data?.let {
                if (isInMyMist(myList, it.id)) {
                    it.is_love = true
                }
            }
        }

        return resource
    }

    suspend fun fetchMoviesWithGenres() : Resource<List<MoviesWithGenre>> {
        val mMoviesWithGenre : MutableList<MoviesWithGenre> = mutableListOf()
        val genresResource = netWorkService.getGenres()
        if(genresResource.status == ResourceStatus.SUCCESS) {
            genresResource.data?.genres?.subList(0, 10)?.forEachIndexed { _, genre ->
                val moviesResource = netWorkService.getMoviesWithGenres(genre.id.toString())
                if(moviesResource.status == ResourceStatus.SUCCESS) {
                    moviesResource.data?.results?.let { movieList ->
                        val moviesWithGenre = MoviesWithGenre(genre.id, genre.name, movieList)
                        mMoviesWithGenre.add(moviesWithGenre)
                    }
                }
            }
        }
        if(mMoviesWithGenre.size > 0) {
            return Resource.Success(mMoviesWithGenre)
        } else {
            return Resource.Error(ApiError())
        }
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

    fun isInMyMist(list : List<LoveMovieId>, id: Int): Boolean {
        list.forEach {
            if(it.id == id) {
                return true
            }
        }
        return false
    }
}

const val ITEMS_PER_PAGE = 20