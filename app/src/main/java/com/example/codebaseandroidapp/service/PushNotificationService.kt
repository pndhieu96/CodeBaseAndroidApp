package com.example.codebaseandroidapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.example.codebaseandroidapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService : FirebaseMessagingService() {
    private val NOTIFICATION_CHANNEL_ID = "HEADS_UP_NOTIFICATION"
    private val NOTIFICATION_CHANNEL_NAME = "HEADS_UP_NOTIFICATION"

    @Override
    override fun onMessageReceived(message: RemoteMessage) {
        val title: String = message.notification?.title.toString()
        val text: String = message.notification?.body.toString()
        Log.d("PushNotificationService", "onMessageReceived title=\'$title\'")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
            val notification = Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title + " 1234123512351235")
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)

            NotificationManagerCompat.from(this).notify(1, notification.build())
        }
        super.onMessageReceived(message)
    }
}