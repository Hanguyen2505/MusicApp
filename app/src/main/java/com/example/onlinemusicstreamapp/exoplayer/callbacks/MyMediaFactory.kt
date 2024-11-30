package com.example.onlinemusicstreamapp.exoplayer.callbacks

import android.support.v4.media.MediaBrowserCompat
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.example.onlinemusicstreamapp.database.data.entities.Artist
import com.example.onlinemusicstreamapp.database.data.entities.Genre
import com.example.onlinemusicstreamapp.database.data.entities.Playlist
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.database.data.entities.UserPlaylist
import com.example.onlinemusicstreamapp.exoplayer.service.music.MusicServiceConnection
import javax.inject.Inject

class MyMediaFactory @OptIn(UnstableApi::class)
@Inject constructor(
    private val musicServiceConnection: MusicServiceConnection
) {
    @OptIn(UnstableApi::class)
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

    @OptIn(UnstableApi::class)
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

    @OptIn(UnstableApi::class)
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

    @OptIn(UnstableApi::class)
    fun fetchPlaylistFromMediaBrowser(
        parentId: String,
        callback: (List<Playlist>) -> Unit
    ) {
        musicServiceConnection.subscribe(parentId, object : MediaBrowserCompat.SubscriptionCallback() {
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                val playlist = children.map { mediaItem ->
                    Playlist(
                        mediaItem.mediaId!!,
                        mediaItem.description.title.toString(),
                        mediaItem.description.description.toString(),
                        mediaItem.description.subtitle!!.split(","),
                        mediaItem.description.iconUri.toString(),
                    )
                }
                callback(playlist)
            }
        })
    }

//    @OptIn(UnstableApi::class)
//    fun fetchUserPlaylistFromMediaBrowser(
//        parentId: String,
//        callback: (List<UserPlaylist>) -> Unit
//    ) {
//        musicServiceConnection.subscribe(
//            parentId,
//            object : MediaBrowserCompat.SubscriptionCallback() {
//                override fun onChildrenLoaded(
//                    parentId: String,
//                    children: MutableList<MediaBrowserCompat.MediaItem>
//                ) {
//                    val userPlaylist = children.map {
//                        UserPlaylist(
//                            it.mediaId!!,
//                            it.description.title.toString(),
//                            it.description.description.toString(),
//                            it.description.iconUri.toString(),
//                            it.description.subtitle.toString().split(","),
//                            it.description.mediaUri.toString()
//                        )
//                    }
//                    callback(userPlaylist)
//                }
//            })
//    }
}