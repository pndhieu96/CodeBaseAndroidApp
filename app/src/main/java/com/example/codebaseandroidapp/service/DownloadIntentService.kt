package com.example.codebaseandroidapp.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.codebaseandroidapp.utils.Utils

/**
 * (UnBound Service)
 * Service-2.1 Intent Service:
 * Để thực hiện các nhiệm vụ một lần duy nhất, nghĩa là khi thực hiện xong
 * nhiệm vụ của mình thì dịch vụ sẽ tự huỷ
 */
@Suppress("DEPRECATION")
class DownloadIntentService : IntentService("DownloadIntentService") {

    companion object {
        private const val TAG = "DownloadIntentService"

        private const val ACTION_DOWNLOAD = "ACTION_DOWNLOAD"
        private const val ACTION_DELETE = "ACTION_DELETE"

        private const val EXTRA_URL = "EXTRA_URL"

        const val DOWNLOAD_COMPLETE = "DOWNLOAD_COMPLETE"

        const val DOWNLOAD_COMPLETE_KEY = "DOWNLOAD_COMPLETE_KEY"

        fun startActionDownload(context: Context, param: String) {
            val intent = Intent(context, DownloadIntentService::class.java).apply {
                action = ACTION_DOWNLOAD
                putExtra(EXTRA_URL, param)
            }
            context.startService(intent)
        }

        fun startActionDelete(context: Context, param: String) {
            val intent = Intent(context, DownloadIntentService::class.java).apply {
                action = ACTION_DELETE
                putExtra(EXTRA_URL, param)
            }
            context.startService(intent)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
    }

    @Deprecated("Deprecated in Java")
    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_DOWNLOAD -> {
                intent.getStringExtra(EXTRA_URL)?.let {
                    handleActionDownload(it)
                }
            }
            ACTION_DELETE -> {
                intent.getStringExtra(EXTRA_URL)?.let {
                    handleActionDelete(it)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onDestroy() {
        Log.i(TAG, "onDestroy")
        super.onDestroy()
    }

    private fun handleActionDownload(param: String) {
        Log.i(TAG, "Starting download for $param")
        Utils.download(param)
        Log.i(TAG, "Ending download for $param")

        Log.i(TAG, "Sending broadcast for $param")
        broadcastDownloadComplete(param)
    }

    private fun handleActionDelete(param: String) {
        Log.i(TAG, "Starting download for $param")
        Utils.delete(Utils.songDirectory())
        Log.i(TAG, "Ending download for $param")

        Log.i(TAG, "Sending broadcast for $param")
        broadcastDownloadComplete(param)
    }

    private fun broadcastDownloadComplete(param: String) {
        val intent = Intent(DOWNLOAD_COMPLETE)
        intent.putExtra(DOWNLOAD_COMPLETE_KEY, param)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }
}
