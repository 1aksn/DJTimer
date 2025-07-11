package com.example.djtimer.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.djtimer.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "dj_timer_channel"
        val channel =
            NotificationChannel(channelId, "DJ Timer", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)


        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("5 minutes until your turn!!")
            .setContentText("この通知を押してもアプリは開かないお")
            .setSmallIcon(R.drawable.ic_foreground)
            .build()

        notificationManager.notify(1001, notification)
    }
}