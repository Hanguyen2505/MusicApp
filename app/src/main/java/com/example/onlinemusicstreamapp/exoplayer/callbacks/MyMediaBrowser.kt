package com.example.onlinemusicstreamapp.exoplayer.callbacks

import android.support.v4.media.MediaBrowserCompat
import com.example.onlinemusicstreamapp.database.data.entities.Artist
import com.example.onlinemusicstreamapp.database.data.entities.Genre
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.exoplayer.service.music.MusicServiceConnection
import javax.inject.Inject

class MyMediaBrowser @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection
) {
    fun fetchSongsFromMediaBrowser(
        parentId: String,
        callback: (List<Song>) -> Unit
    ) {
        musicServiceConnection.subscribe(
            parentId,
            object : MediaBrowserCompat.SubscriptionCallback() {
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>
                ) {
                    val songs = children.map {
                        Song(
                            it.mediaId!!,
                            it.description.title.toString(),
                            it.description.subtitle.toString().split(","),
                            it.description.mediaUri.toString(),
                            it.description.iconUri.toString(),
                            it.description.description.toString().split(",")
                        )
                    }
                    callback(songs)
                }
            })
    }

    fun fetchArtistFromMediaBrowser(
        parentId: String,
        callback: (List<Artist>) -> Unit
    ) {
        musicServiceConnection.subscribe(parentId, object : MediaBrowserCompat.SubscriptionCallback() {
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                val artist = children.map {
                    Artist(
                        it.mediaId!!,
                        it.description.title.toString(),
                        it.description.iconUri.toString()
                    )
                }
                callback(artist)
            }
        })
    }

    fun fetchGenreFromMediaBrowser(
        parentId: String,
        callback: (List<Genre>) -> Unit
    ) {
        musicServiceConnection.subscribe(parentId, object : MediaBrowserCompat.SubscriptionCallback() {
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                val genre = children.map {
                    Genre(
                        it.mediaId!!,
                        it.description.title.toString(),
                        it.description.iconUri.toString(),
                        it.description.description.toString(),
                    )
                }
                callback(genre)
            }
        })
    }
}