package com.example.onlinemusicstreamapp.exoplayer.service.music

import android.app.Notification
import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerNotificationManager

class MusicNotificationManager @OptIn(UnstableApi::class)
    (
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    notification: Notification.MediaStyle
) {
        
}