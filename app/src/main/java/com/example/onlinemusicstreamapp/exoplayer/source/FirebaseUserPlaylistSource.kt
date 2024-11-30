package com.example.onlinemusicstreamapp.exoplayer.source

import android.media.MediaMetadata.METADATA_KEY_ALBUM
import android.media.MediaMetadata.METADATA_KEY_TITLE
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI
import androidx.core.net.toUri
import com.example.onlinemusicstreamapp.database.data.remote.UserPlaylistDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseUserPlaylistSource @Inject constructor(
    private val userPlaylistDatabase: UserPlaylistDatabase
) {
    var playlists = emptyList<MediaMetadataCompat>()

    suspend fun fetchMediaData() = withContext(Dispatchers.IO) {
        val allPlaylists = userPlaylistDatabase.getUserPlaylist()
        playlists = allPlaylists.map { userPlaylist ->
            MediaMetadataCompat.Builder()
                .putString(METADATA_KEY_TITLE, userPlaylist.title)
                .putString(METADATA_KEY_DISPLAY_DESCRIPTION, userPlaylist.description)
                .putString(METADATA_KEY_MEDIA_ID, userPlaylist.id)
                .putString(METADATA_KEY_DISPLAY_ICON_URI, userPlaylist.coverUrl)
                .putString(METADATA_KEY_ALBUM_ART_URI, userPlaylist.coverUrl)
                .putString(METADATA_KEY_ALBUM, userPlaylist.songIds.joinToString(","))
                .build()
        }

    }

    fun asMediaItems() = playlists.map { playlist ->
        val desc = MediaDescriptionCompat.Builder()
            .setTitle(playlist.description.title)
            .setDescription(playlist.description.description)
            .setMediaId(playlist.description.mediaId)
            .setIconUri(playlist.getString(METADATA_KEY_DISPLAY_ICON_URI).toUri())
            .setSubtitle(playlist.getString(METADATA_KEY_ALBUM))
            .build()
        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }.toMutableList()
}