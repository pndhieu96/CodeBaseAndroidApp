package com.example.codebaseandroidapp.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.codebaseandroidapp.Application
import com.example.codebaseandroidapp.MainActivity
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.utils.Utils
import android.widget.RemoteViews
import android.app.PendingIntent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.codebaseandroidapp.model.MusicControl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
/**
 * Service là 1 thành phần có thể thực hiện các tác vụ lâu dài trong backgroud và nó không cung
 * cấp 1 giao diện người dùng, Và có thể tiếp tục chạy trong background ngay cả khi người dùng chuyển
 * sang ứng dụng khác hay ứng dụng bị phá huỷ
 *
 * Có 3 loại service cơ bản:
 * Service-1. Foreground service:
 * Thực hiện 1 số tác vụ mà người dùng chú ý, có thể thấy rõ ràng. Ví dụ như có thể chơi 1 một bản nhạc và
 * control nó bằng Foreground Service. Foreground Service bắt buộc phải hiển thị một Notification. Foreground
 * Service có thể tiếp tục chạy ngay cả khi người dùng không tương tác với ứng dụng, ứng dụng chạy trong nền, hãy
 * đã bị tắt bởi người dùng.
 */
class SongService : Service() {
    companion object {
        const val CHANNEL_ID = "media_playback_channel"
        const val NOTIFICATION_ID = 123
        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_CREATE = "ACTION_CREATE"
        const val NOTIFICATION_ACTION = "NOTIFICATION_ACTION"
    }

    @Inject
    lateinit var player: MusicControl

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val broadcastIntent = Intent(NOTIFICATION_ACTION)
            when(it.action) {
                ACTION_CREATE -> {
                    player.playMusic(Uri.fromFile(Utils.songFile()), true)
                    Application.isPlayingSong = true
                    broadcastIntent.putExtra("ACTION", ACTION_CREATE)
                }
                ACTION_PLAY -> {
                    player.playMusic()
                    Application.isPlayingSong = true
                    broadcastIntent.putExtra("ACTION", ACTION_PLAY)
                }
                ACTION_PAUSE -> {
                    player.pauseMusic()
                    Application.isPlayingSong = false
                    broadcastIntent.putExtra("ACTION", ACTION_PAUSE)
                }
                else -> {
                    player.playMusic(Uri.fromFile(Utils.songFile()), true)
                    Application.isPlayingSong = true
                }
            }
            LocalBroadcastManager.getInstance(Application.getAppContext()).sendBroadcast(broadcastIntent)
        }
        startForeground(NOTIFICATION_ID, Utils.createNotification(this))
        return START_STICKY
    }

    override fun onDestroy() {
        player?.stopMusic()
        Application.isPlayingSong = false
        super.onDestroy()
    }
}