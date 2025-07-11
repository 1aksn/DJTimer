package com.example.djtimer.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import java.time.LocalDateTime
import java.time.ZoneId
import android.provider.Settings

object NotificationHelper {
    const val PREF_NAME = "timer_prefs"
    const val KEY_NOTIFICATION_SCHEDULED = "notification_scheduled"

    fun scheduleNotification(context: Context, startTime: LocalDateTime) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        if (prefs.getBoolean(KEY_NOTIFICATION_SCHEDULED, false)) {
            return
        }
        // ✅ Android 12以降は、許可がないと設定できない
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(AlarmManager::class.java)
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                intent.data = Uri.parse("package:${context.packageName}")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                return  // 許可取るまではスケジュールしない
            }
        }

        // 🔥 許可OKなので、通知をスケジュール
        val triggerTimeMillis = startTime.minusMinutes(5)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent)

        prefs.edit().remove(KEY_NOTIFICATION_SCHEDULED).apply()
    }

    fun resetNotificationFlag(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_NOTIFICATION_SCHEDULED).apply()
    }
}