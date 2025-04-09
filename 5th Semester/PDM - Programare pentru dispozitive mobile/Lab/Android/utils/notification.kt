package com.example.smartnote.utils

import com.example.smartnote.MainActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat

// Funcție pentru a verifica și solicita permisiunea de notificare pe Android 13+
fun checkAndRequestNotificationPermission(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {  // Android 13+ (API 33+)
        val permission = Manifest.permission.POST_NOTIFICATIONS
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            // Permisiunea nu este acordată, o cerem
            if (context is MainActivity) {
                ActivityCompat.requestPermissions(context, arrayOf(permission), 1)
            }
        } else {
            Log.d("Notification", "Permission to post notifications is already granted.")
        }
    }
}

fun createNotificationChannel(channelId: String, context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val existingChannel = notificationManager.getNotificationChannel(channelId)
        Log.d("Notification", "Notification channel exists: ${existingChannel?.name}")
        if (existingChannel == null) {
            Log.d("Notification", "Creating new notification channel with ID: $channelId")
            val name = "MyTestChannel"
            val descriptionText = "My important test channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        } else {
            Log.d("Notification", "Notification channel already exists: $channelId")
        }
    } else {
        Log.d("Notification", "Notification channel creation is not required for SDK < O")
    }
}

fun showSimpleNotification(
    context: Context,
    channelId: String,
    notificationId: Int,
    textTitle: String,
    textContent: String
) {
    createNotificationChannel(channelId, context)
    Log.d("Notification", "Building notification with title: $textTitle and content: $textContent")

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_notification_overlay)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    Log.d("Notification", "Sending notification with ID: $notificationId")
    val notificationManagerCompat = NotificationManagerCompat.from(context)
    notificationManagerCompat.notify(notificationId, notification)
    Log.d("Notification", "Notification sent")
}


fun showSimpleNotificationWithTapAction(
    context: Context,
    channelId: String,
    notificationId: Int,
    textTitle: String,
    textContent: String,
    noteId: String  // ID-ul notei
) {
    // Creăm un intent care deschide MainActivity și trimite ID-ul notei
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        putExtra("NOTE_ID", noteId)  // Adăugăm ID-ul notei în intenție
    }

    Log.d("Notification", "Building notification with noteId $noteId")

    // Folosim PendingIntent.FLAG_IMMUTABLE pentru Android 12 și ulterior
    val pendingIntent: PendingIntent = PendingIntent.getActivity(
        context, 0, intent, PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_notification_overlay)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent) // Atașăm PendingIntent-ul
        .setAutoCancel(true)  // Închide notificarea după apăsare

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())  // Trimitem notificarea
    }
}
