package com.example.onlinemusicstreamapp.exoplayer.source

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import androidx.core.net.toUri
import com.example.onlinemusicstreamapp.database.data.remote.PlaylistDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebasePlaylistSource @Inject constructor(
    private val playlistDatabase: PlaylistDatabase
) {
    var playlists = emptyList<MediaMetadataCompat>()

    suspend fun fetchMediaData() = withContext(Dispatchers.IO) {
        val allPlaylists = playlistDatabase.getAllPlaylists()
        playlists = allPlaylists.map { playlist ->
            Builder()
                .putString(METADATA_KEY_MEDIA_ID, playlist.playlistId)
                .putString(METADATA_KEY_TITLE, playlist.title)
                .putString(METADATA_KEY_DISPLAY_TITLE, playlist.title)
                .putString(METADATA_KEY_DISPLAY_ICON_URI, playlist.coverImageUrl)
                .putString(METADATA_KEY_DISPLAY_DESCRIPTION, playlist.description)
                .putString(METADATA_KEY_DISPLAY_SUBTITLE, playlist.songIds.joinToString(","))
                .build()
        }

    }

    fun asMediaItems() = playlists.map { playlist ->
        val desc = MediaDescriptionCompat.Builder()
            .setIconUri(playlist.getString(METADATA_KEY_DISPLAY_ICON_URI).toUri())
            .setTitle(playlist.description.title)
            .setDescription(playlist.description.description)
            .setMediaId(playlist.description.mediaId)
            .setSubtitle(playlist.getString(METADATA_KEY_DISPLAY_SUBTITLE))
            .build()
        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }.toMutableList()
}
