package com.example.codebaseandroidapp.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.codebaseandroidapp.model.Movie

/*
* Paging-1
* Sử dụng để sử lý phân trang bao gồm các thành phần chính:
    * PagingData: chứa dữ liệu đã được paging. Mỗi lần refresh data sẽ có một PadingData tương ứng.
    * PagingSource: là một lớp cơ sở để xác định cách load dữ liệu trong phương thức load và trả
        * về LoadResult để chứa dữ liệu của page hiện tại hoặc lỗi nếu load dữ liệu không thành công
    * Pager.flow: build 1 Flow<PagingData> dựa trên PagingConfig và một phương thức để xác định cách khởi
        * tạo PagingSource
    * PagingDataApdater: là một RecyclerView.Adapter để đại diện cho PagingData trong một RecyclerView.
        * Nó có thể kết nối với 1 Flow, liveData hoặc RxJava Flowable, Observable. Nó lắng nghe sự kiện
        * tải dữ liệu PagingData đã được load và hiển thị kết quả lên cho người dùng
    * RemoteMediator: giúp triển khai phân trang từ network và database
**/

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

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        TODO("Not yet implemented")
    }

}