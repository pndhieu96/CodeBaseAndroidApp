package com.example.codebaseandroidapp.network

import androidx.paging.PagingSource
import com.example.codebaseandroidapp.model.Movie

class MoviePagingSource(
    val netWorkService: NetWorkService,
    val query: String
): PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        try {
            val nextPage = params.key ?: 1
            val queryString = if(query.isEmpty()) "p" else  query
            val response = netWorkService.searchMovies(queryString, nextPage)

            return if(response.total_results == 0){
                return LoadResult.Error(Exception("-1"))
            } else {
                LoadResult.Page(
                    data = response.results,
                    prevKey = if(nextPage == 1) null else nextPage -1,
                    nextKey = nextPage + 1
                )
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}