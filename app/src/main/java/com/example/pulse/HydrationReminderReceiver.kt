package com.example.pulse

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.startActivity
import java.util.Calendar

class HydrationReminderReceiver : BroadcastReceiver() {

    @SuppressLint("LaunchActivityFromNotification")
    override fun onReceive(context: Context, intent: Intent) {

        Log.d("HydrationReminder", "Alarm triggered!")

        // Create notification
        val channelId = "hydration_channel"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Hydration Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val openIntent = Intent(context, HydrationScreen::class.java)
        val pendingIntentForNotification = PendingIntent.getActivity(
            context,
            0,
            openIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.watercup)
            .setContentTitle("Time to drink water 💧")
            .setContentText("Stay hydrated! Tap to log your water intake.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntentForNotification)
            .build()

        notificationManager.notify(1, notification)

        val prefs = context.getSharedPreferences("hydration", Context.MODE_PRIVATE)
        val hour = prefs.getInt("reminderHour", 9)
        val minute = prefs.getInt("reminderMinute", 0)


        Log.d("HydrationReminder", "Next reminder scheduled for $hour:$minute tomorrow")


    }

}
