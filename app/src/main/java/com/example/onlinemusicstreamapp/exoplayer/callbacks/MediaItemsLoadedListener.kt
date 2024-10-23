package com.example.onlinemusicstreamapp.exoplayer.callbacks

import android.support.v4.media.MediaBrowserCompat

interface MediaItemsLoadedListener {
    fun onMediaItemsLoaded(mediaItems: List<MediaBrowserCompat.MediaItem>)
}