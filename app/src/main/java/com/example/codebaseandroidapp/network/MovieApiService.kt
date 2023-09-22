package com.example.codebaseandroidapp.network

import com.example.codebaseandroidapp.base.BaseRepo
import com.example.codebaseandroidapp.base.Resource
import com.example.codebaseandroidapp.model.*
import com.example.codebaseandroidapp.utils.ConstantUtils.Companion.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

// Một retrofit service để xử lý các http request tới web service để lấy dữ liệu
// và nhận dữ liệu trả về
class NetWorkService //Khởi tạo 1 Service để gửi request
constructor(retrofit: Retrofit) : BaseRepo() {
    private val BASE_URL = "https://api.themoviedb.org/3/"
    private var movieApiService: MovieApiService? = null

    init {
        movieApiService = retrofit.create(MovieApiService::class.java)
    }

    suspend fun getRelativeMovies(id: String): Resource<Movies> {
        return safeApiCall { movieApiService?.getRelativeMovies(id)!! }
    }

    suspend fun getDetail(id: String): Resource<Detail> {
        return safeApiCall { movieApiService?.getDetail(id)!! }
    }

    suspend fun getDetailBaseVM(id: String): Response<Detail> {
        return  movieApiService?.getDetail(id)!!
    }

    suspend fun getGenres(): Resource<Genres> {
        return safeApiCall { movieApiService?.getGenres()!! }
    }

    suspend fun getMoviesWithGenres(id: String): Resource<Movies> {
        return safeApiCall { movieApiService?.getMoviesWithGenre(id)!! }
    }

    suspend fun searchMovies(query: String, page: Int): Movies {
        return withContext(Dispatchers.IO) {
            movieApiService?.searchMovies(query, page)!!
        }
    }
}

//Khai báo các http request
// Bao gồm các HTTP operation như: GET, POST, PUT, DELETE
interface MovieApiService {
    //@QUERY để truyền parrams trên đường dẫn cho phương thức GET
    @GET("discover/movie?api_key=990c4fbb01df42398dcb580b5d8b271e&page=1")
    suspend fun getRelativeMovies(@Query("with_keywords") id: String): Response<Movies>
    ////
    @GET("genre/movie/list?api_key=${API_KEY}&language=en-US")
    suspend fun getGenres(): Response<Genres>

    @GET("discover/movie?api_key=990c4fbb01df42398dcb580b5d8b271e&page=1")
    suspend fun getMoviesWithGenre(@Query("with_genres") genreId: String): Response<Movies>

    @GET("movie/{movieId}?api_key=990c4fbb01df42398dcb580b5d8b271e")
    suspend fun getDetail(@Path("movieId") movieId: String): Response<Detail>

    @GET("search/movie?api_key=990c4fbb01df42398dcb580b5d8b271e&language=en-US")
    suspend fun searchMovies(@Query("query") query: String, @Query("page") page: Int): Movies
}

//Một đối tượng api công khai để khởi tạo (lazy) service
// với lazy, đối tượng sẽ được khởi tạo ở lần đầy sử dụng
//object MovieApi {
//    val retrofitService: MovieApiService by lazy {
//        NetWorkService.retrofit.create(MovieApiService::class.java)
//    }
//}
