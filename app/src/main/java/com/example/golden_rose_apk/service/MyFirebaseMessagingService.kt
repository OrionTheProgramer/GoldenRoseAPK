package com.example.golden_rose_apk.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.example.golden_rose_apk.R


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("FCM", "Nuevo token generado: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("FCM", "Mensaje recibido: ${message.data}")

        // Notificación simple
        val notification = message.notification
        if (notification != null) {
            showNotification(
                title = notification.title ?: "Notificación",
                message = notification.body ?: "Mensaje recibido"
            )
        }
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "push_channel"

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Canal para Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notificaciones",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification)
            .setAutoCancel(true)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}

