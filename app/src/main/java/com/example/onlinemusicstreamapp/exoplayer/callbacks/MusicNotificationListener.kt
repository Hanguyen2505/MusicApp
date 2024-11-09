package com.example.onlinemusicstreamapp.exoplayer.callbacks

import android.app.Notification
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerNotificationManager
import com.example.onlinemusicstreamapp.exoplayer.service.music.MusicService

@UnstableApi
class MusicNotificationListener @OptIn(UnstableApi::class) constructor
    (
    private val musicService: MusicService
): PlayerNotificationManager.NotificationListener {
    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)

        musicService.apply {
            stopForeground(true)
            stopSelf()
        }
    }

    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        super.onNotificationPosted(notificationId, notification, ongoing)

        musicService.apply {
            if (ongoing) {
                startForeground(notificationId, notification)
                stopSelf()
            }
        }
    }

}