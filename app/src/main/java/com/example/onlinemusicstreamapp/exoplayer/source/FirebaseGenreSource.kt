package com.example.onlinemusicstreamapp.exoplayer.source

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_GENRE
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_TITLE
import androidx.core.net.toUri
import com.example.onlinemusicstreamapp.database.data.remote.GenreDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseGenreSource @Inject constructor(
    private val genreDatabase: GenreDatabase
) {
    var genres = emptyList<MediaMetadataCompat>()

    suspend fun fetchMediaData() = withContext(Dispatchers.IO) {
        val allGenres = genreDatabase.getAllGenre()
        genres = allGenres.map { genre ->
            MediaMetadataCompat.Builder()
                .putString(METADATA_KEY_MEDIA_ID, genre.genreId)
                .putString(METADATA_KEY_TITLE, genre.name)
                .putString(METADATA_KEY_DISPLAY_TITLE, genre.name)
                .putString(METADATA_KEY_DISPLAY_ICON_URI, genre.genreImg)
                .putString(METADATA_KEY_GENRE, genre.name)
                .putString(METADATA_KEY_DISPLAY_DESCRIPTION, genre.color)
                .build()
        }

    }

    fun asMediaItems() = genres.map { genre ->
        val desc = MediaDescriptionCompat.Builder()
            .setIconUri(genre.getString(METADATA_KEY_DISPLAY_ICON_URI).toUri())
            .setTitle(genre.description.title)
            .setDescription(genre.description.description)
            .setMediaId(genre.description.mediaId)
            .build()
        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }.toMutableList()
}
