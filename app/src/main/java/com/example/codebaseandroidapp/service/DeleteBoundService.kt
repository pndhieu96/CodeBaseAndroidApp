package com.example.codebaseandroidapp.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.codebaseandroidapp.utils.Utils

/**
 * (Bound Service)
 * Service-3. Bound Service:
 * Service là bound khi một thành phần của ứng dụng ràng buộc với nó bằng lời gọi
 * bindService(). Một Bound Service cung cấp một giao diện client-server cho phép các thành phần tương
 * tác với nó và nhận kết quả. Một Bound Service chạy khi có ít nhất một thành phần ràng buộc với nó.
 * Có thể có nhiều thành phần ràng buộc với Bound Service cùng lúc, nhưng khi tất tháo bỏ ràng buộc với
 * lời gọi UnBound thì nó sẽ Destroy.
 */
class DeleteBoundService : Service() {

    private val mBinder: IBinder = DeleteBoundServiceBinder()

    inner class DeleteBoundServiceBinder : Binder() {
        fun getService(): DeleteBoundService = this@DeleteBoundService
    }

    override fun onCreate() {
        Log.i("DeleteBoundService", "onCreate")
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.i("DeleteBoundService", "onBind")
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i("DeleteBoundService", "onUnbind")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Log.i("DeleteBoundService", "onDestroy")
        super.onDestroy()
    }

    fun handleActionDelete(param: String) : Boolean {
        Log.i("DeleteBoundService", "Starting delete for $param")
        Utils.delete(Utils.songDirectory())
        Log.i("DeleteBoundService", "Ending delete for $param")

        Log.i("DeleteBoundService", "Sending delete for $param")
        return true
    }
}