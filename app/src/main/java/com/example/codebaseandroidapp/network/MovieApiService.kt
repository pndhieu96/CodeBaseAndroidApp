package com.example.codebaseandroidapp.network

import com.example.codebaseandroidapp.model.Detail
import com.example.codebaseandroidapp.model.Genres
import com.example.codebaseandroidapp.model.Movies
import com.example.codebaseandroidapp.model.MoviesWithGenre
import com.example.codebaseandroidapp.utils.ConstantUtils.Companion.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

//Một retrofit service để xử lý các http request tới web service để lấy dữ liệu
// và nhận dữ liệu trả về
class NetWorkService {
    private val BASE_URL = "https://api.themoviedb.org/3/"
    private var movieApiService: MovieApiService? = null

    @Inject constructor(
        retrofit: Retrofit
    ) {
        //Khởi tạo 1 Service để gửi request
        movieApiService = retrofit.create(MovieApiService::class.java)
    }

    suspend fun getGenres(): Genres {
        return withContext(Dispatchers.IO) {
            movieApiService?.getGenres()!!
        }
    }

    suspend fun getMoviesWithGenre(id: String): Movies {
        return withContext(Dispatchers.IO) {
            movieApiService?.getMoviesWithGenre(id)!!
        }
    }

    suspend fun getDetail(id: String): Detail {
        return withContext(Dispatchers.IO) {
            movieApiService?.getDetail(id)!!
        }
    }

    suspend fun searchMovies(query: String, page: Int): Movies {
        return withContext(Dispatchers.IO) {
            movieApiService?.searchMovies(query, page)!!
        }
    }

    suspend fun getRelativeMovie(id: String): Movies {
        return withContext(Dispatchers.IO) {
            movieApiService?.getRelativeMovies(id)!!
        }
    }
}

//Khai báo các http request
// Bao gồm các HTTP operation như: GET, POST, PUT, DELETE
interface MovieApiService {
    //@QUERY để truyền parrams trên đường dẫn cho phương thức GET
    @GET("genre/movie/list?api_key=${API_KEY}&language=en-US")
    suspend fun getGenres(): Genres

    @GET("discover/movie?api_key=990c4fbb01df42398dcb580b5d8b271e&page=1")
    suspend fun getMoviesWithGenre(@Query("with_genres") genreId: String): Movies

    @GET("discover/movie?api_key=990c4fbb01df42398dcb580b5d8b271e&page=1")
    suspend fun getRelativeMovies(@Query("with_keywords") id: String): Movies

    @GET("search/movie?api_key=990c4fbb01df42398dcb580b5d8b271e&language=en-US")
    suspend fun searchMovies(@Query("query") query: String, @Query("page") page: Int): Movies

    @GET("movie/{movieId}?api_key=990c4fbb01df42398dcb580b5d8b271e")
    suspend fun getDetail(@Path("movieId") movieId: String): Detail
}

//Một đối tượng api công khai để khởi tạo (lazy) service
// với lazy, đối tượng sẽ được khởi tạo ở lần đầy sử dụng
//object MovieApi {
//    val retrofitService: MovieApiService by lazy {
//        NetWorkService.retrofit.create(MovieApiService::class.java)
//    }
//}
