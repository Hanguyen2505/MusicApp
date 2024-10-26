package com.example.onlinemusicstreamapp.exoplayer.source

import android.media.MediaMetadata.METADATA_KEY_ARTIST
import android.media.MediaMetadata.METADATA_KEY_TITLE
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI
import androidx.core.net.toUri
import com.example.onlinemusicstreamapp.database.data.remote.ArtistDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseArtistSource @Inject constructor(
    private val artistDatabase: ArtistDatabase
) {
    var artists = emptyList<MediaMetadataCompat>()

    suspend fun fetchArtistData() = withContext(Dispatchers.IO) {
        val allArtists = artistDatabase.getAllArtist()
        artists = allArtists.map { artist ->
            MediaMetadataCompat.Builder()
                .putString(METADATA_KEY_TITLE, artist.name)
                .putString(METADATA_KEY_ARTIST, artist.name)
                .putString(METADATA_KEY_MEDIA_ID, artist.artistId)
                .putString(METADATA_KEY_DISPLAY_ICON_URI, artist.imageUrl)
                .putString(METADATA_KEY_ALBUM_ART_URI, artist.imageUrl)
                .build()
        }

    }

    fun asMediaItems() = artists.map { artist ->
        val desc = MediaDescriptionCompat.Builder()
            .setTitle(artist.description.title)
            .setSubtitle(artist.getString(METADATA_KEY_ARTIST))
            .setMediaId(artist.description.mediaId)
            .setIconUri(artist.getString(METADATA_KEY_DISPLAY_ICON_URI).toUri())
            .build()
        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }.toMutableList()
}