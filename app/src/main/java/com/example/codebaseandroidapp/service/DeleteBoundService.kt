package com.example.codebaseandroidapp.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.codebaseandroidapp.utils.Utils

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

    public fun handleActionDelete(param: String) : Boolean {
        Log.i("DeleteBoundService", "Starting delete for $param")
        Utils.delete(Utils.songDirectory())
        Log.i("DeleteBoundService", "Ending delete for $param")

        Log.i("DeleteBoundService", "Sending delete for $param")
        return true
    }
}