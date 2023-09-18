package com.example.codebaseandroidapp.di

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Room
import com.example.codebaseandroidapp.adapter.HomeParentRecycleViewAdapter
import com.example.codebaseandroidapp.adapter.RelativeMoviesAdapter
import com.example.codebaseandroidapp.adapter.SearchAdapter
import com.example.codebaseandroidapp.callBack.MovieDiffCallback
import com.example.codebaseandroidapp.callBack.MoviesWithGenreDiffCallback
import com.example.codebaseandroidapp.database.AppDatabase
import com.example.codebaseandroidapp.database.DATABASE_NAME
import com.example.codebaseandroidapp.model.MoviesWithGenre
import com.example.codebaseandroidapp.network.NetWorkService
import com.example.codebaseandroidapp.repository.MovieRepository
import com.example.codebaseandroidapp.utils.ConstantUtils.Companion.BASE_URL
import com.example.codebaseandroidapp.viewModel.DetailViewModel
import com.example.codebaseandroidapp.viewModel.HomeRootViewModel
import com.example.codebaseandroidapp.viewModel.HomeViewModel
import com.example.codebaseandroidapp.viewModel.SearchViewModel
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class KoinApplicationModule {
    companion object {
        val retrofitModule = module {
            fun provideGson(): Gson {
                return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
            }

            fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                return interceptor
            }

            fun provideOkHttpClient (
                interceptor: HttpLoggingInterceptor
            ): OkHttpClient {
                return OkHttpClient.Builder().addInterceptor(interceptor).build()
            }

            fun provideRetrofit(
               factory: Gson,
               client: OkHttpClient
            ): Retrofit {
                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(factory))
                    .client(client)
                    .build()
            }

            single { provideGson() }
            single { provideHttpLoggingInterceptor() }
            single { provideOkHttpClient(get()) }
            single { provideRetrofit(get(), get()) }
        }

        val apiModule = module {
            fun provideUseApi(retrofit: Retrofit): NetWorkService {
                return NetWorkService(retrofit)
            }

            single { provideUseApi(get()) }
        }

        val databaseModule = module {
            fun provideDatabaseModule(context: Context): AppDatabase {
                return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
            }
            single { provideDatabaseModule(get()) }
        }

        val repositoryModule = module {
            fun provideRepository(database: AppDatabase, netWorkService: NetWorkService): MovieRepository {
                return MovieRepository(database, netWorkService)
            }
            single { provideRepository(get(), get()) }
        }

        val viewModelModule = module {
            viewModel {
                DetailViewModel(get())
            }
            viewModel {
                HomeViewModel(get())
            }
            viewModel {
                SearchViewModel(get())
            }
            viewModel {
                HomeRootViewModel()
            }
        }

        val diffCallbackModule = module {
            fun provideMovieDiffCallback(): MovieDiffCallback {
                return MovieDiffCallback()
            }
            fun provideMoviesWithGenreDiffCallback(): MoviesWithGenreDiffCallback {
                return MoviesWithGenreDiffCallback()
            }
            single { provideMovieDiffCallback() }
            single { provideMoviesWithGenreDiffCallback() }
        }

        val adapterModule = module {
            fun provideRelativeMoviesAdapter(): RelativeMoviesAdapter {
                return RelativeMoviesAdapter()
            }
            fun provideHomeParentRecycleViewAdapter(movieCallBack: MoviesWithGenreDiffCallback): HomeParentRecycleViewAdapter {
                return HomeParentRecycleViewAdapter(movieCallBack)
            }
            fun provideSearchAdapter(): SearchAdapter {
                return SearchAdapter()
            }
            single { provideRelativeMoviesAdapter() }
            single { provideHomeParentRecycleViewAdapter(get()) }
            single { provideSearchAdapter() }
        }
    }
}