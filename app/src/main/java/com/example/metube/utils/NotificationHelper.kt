package com.example.metube.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.metube.R
import com.example.metube.presentation.VideoPlayerActivity

object NotificationHelper {
    private const val CHANNEL_ID = "video_channel"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Notificações de Vídeo",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para notificações de novos vídeos"
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    fun sendVideoNotification(context: Context) {
        val videoId = "ekr2nIex040" // ID do vídeo de "APT." de ROSÉ & Bruno Mars
        val videoTitle = "APT. - ROSÉ & Bruno Mars"

        val intent = Intent(context, VideoPlayerActivity::class.java).apply {
            putExtra("VIDEO_ID", videoId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("🎵 A música mais ouvida no YouTube!")
            .setContentText("Clique para assistir: $videoTitle")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(1, notification)
    }
}
