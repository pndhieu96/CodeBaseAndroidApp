package com.example.codebaseandroidapp

import android.app.Application
import android.content.Context
import androidx.room.RoomDatabase
import com.example.codebaseandroidapp.database.AppDatabase
import com.example.codebaseandroidapp.network.NetWorkService
import com.example.codebaseandroidapp.repository.MovieRepository
import dagger.hilt.android.HiltAndroidApp

/**
 * Hilt-1
 * Denendency:
 * Khi một class A cần một số chức năng của class B, thì ta nói A có quan hệ phụ thuôc đến B
 *
 * Dependency injection:
 * Là một kỹ thuật mà theo đó một đối tượng (hay một static method) cung cấp các phụ thuộc của đối tượng khác.
 * Một phụ thuộc là một đối tượng có thể sử dụng
    * ví dụ:
        * Ta có thể tạo một container object chứa những phụ thuộc cần thiết sử dụng trong ứng dụng
        * và những nơi cần sử dụng phụ thuộc chi cần gọi đến container object để lấy phụ thuôc tương ứng
 *
 * Hilt:
 * nó là một thư viện giúp dependency injection trong android giúp giảm bới thực hiện dependency injection
 * thủ công trong dự án
 *
 * Để bắt đầu sử dụng hilt ta phải config nó trong build.gradle
 * và chú thích Application với @HiltAndroidApp annotation
    * @HiltAndroidApp để kích hoạt (triggers) việc tạo tự động code của hilt cho ứng dụng
 */
@HiltAndroidApp
class Application : Application() {
    companion object {

        private lateinit var instance: Application

        var isPlayingSong = false

        fun getAppContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}