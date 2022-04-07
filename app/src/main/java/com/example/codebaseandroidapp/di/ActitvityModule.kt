package com.example.codebaseandroidapp.di

import androidx.recyclerview.widget.DiffUtil
import com.example.codebaseandroidapp.adapter.HomeChildLanscapeRecycleViewAdapter
import com.example.codebaseandroidapp.adapter.HomeChildPortraitRecycleViewAdapter
import com.example.codebaseandroidapp.adapter.MovieDiffCallback
import com.example.codebaseandroidapp.adapter.MoviesWithGenreDiffCallback
import com.example.codebaseandroidapp.model.Movie
import com.example.codebaseandroidapp.model.MoviesWithGenre
import com.example.codebaseandroidapp.utils.ConstantUtils.Companion.BASE_URL
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Hilt-5
 * Module:
 * Để cung cấp các instance của các class mà không sử dụng được constructor-injected
 * như là interface, abstrack class hay những class không chứa trong project.
 *
 * Provides:
 * Để cung cấp cách khởi tạo cho những instance của các lass không nằm trong project
 *
 */
@Module
@InstallIn(ActivityRetainedComponent::class)
class ActitvityModule {

    @Provides
    fun provideHttpLogingInterceptror(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    fun provideOkHttpClient (
        interceptor : HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    @Provides
    fun provideRetrofit(
        client : OkHttpClient
    ) : Retrofit {
        //Khởi tạo đối tượng retrofit
        return Retrofit.Builder()
            //Nói với retrofit cách xử lý dữ liệu lấy về từ service
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build()
    }
}

/**
 * Hilt-6
 * Binds:
 * Để cung cấp cách triển khai của những interface hay abstract class
 *
 * Qualifier:
 * Để cung cấp những cách khởi tạo những instances khác nhau cho cùng một kiểu
 */
@Module
@InstallIn(ActivityComponent::class)
abstract class ActitvityAbstractModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MovieItemCallBack

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MovieWithGenreItemCallBack

    @MovieItemCallBack
    @Binds
    abstract fun bindMovieItemCallback(callBack: MovieDiffCallback) : DiffUtil.ItemCallback<Movie>

    @MovieWithGenreItemCallBack
    @Binds
    abstract fun bindMovieWithGenreItemCallback(callBack: MoviesWithGenreDiffCallback) : DiffUtil.ItemCallback<MoviesWithGenre>
}