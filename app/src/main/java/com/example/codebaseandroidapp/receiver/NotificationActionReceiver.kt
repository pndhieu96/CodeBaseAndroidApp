package com.example.codebaseandroidapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.codebaseandroidapp.Application
import com.example.codebaseandroidapp.service.DownloadIntentService
import com.example.codebaseandroidapp.service.SongService
import com.example.codebaseandroidapp.utils.ConstantUtils
import com.example.codebaseandroidapp.utils.Utils

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.getStringExtra("ACTION")?.let {
            Log.i("ActionReceiver", "onReceive: " + intent.getStringExtra("ACTION"))
            when(it) {
                SongService.ACTION_PLAY -> {
                    context?.let { context ->
                        Intent(context, SongService::class.java).let { intent ->
                            intent.setAction(SongService.ACTION_PLAY)
                            ContextCompat.startForegroundService(context, intent)
                        }
                    }
                }
                SongService.ACTION_PAUSE -> {
                    context?.let { context ->
                        Intent(context, SongService::class.java).let { intent ->
                            intent.setAction(SongService.ACTION_PAUSE)
                            ContextCompat.startForegroundService(context, intent)
                        }
                    }
                }
                else -> {

                }
            }
        }
    }
}