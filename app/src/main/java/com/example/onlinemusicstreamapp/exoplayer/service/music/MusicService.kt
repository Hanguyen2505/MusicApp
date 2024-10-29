package com.example.onlinemusicstreamapp.exoplayer.service.music

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media3.common.MediaItem
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_ARTIST_ID
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_GENRE_ID
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_ROOT_ID
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_SONG_ID
import com.example.onlinemusicstreamapp.database.other.Constants.SERVICE_TAG
import com.example.onlinemusicstreamapp.exoplayer.source.FirebaseArtistSource
import com.example.onlinemusicstreamapp.exoplayer.source.FirebaseGenreSource
import com.example.onlinemusicstreamapp.exoplayer.source.FirebaseMusicSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MusicService: MediaBrowserServiceCompat() {

    private var curPlayingSong: MediaMetadataCompat? = null

    private lateinit var mediaSession: MediaSessionCompat

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)


    @Inject
    lateinit var dataSourceFactory: DefaultDataSource.Factory

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var firebaseMusicSource: FirebaseMusicSource

    @Inject
    lateinit var firebaseArtistSource: FirebaseArtistSource

    @Inject
    lateinit var firebaseGenreSource: FirebaseGenreSource

    override fun onCreate() {
        super.onCreate()

        // Set up MediaSession
        mediaSession = MediaSessionCompat(this, SERVICE_TAG).apply {
            setCallback(mediaSessionCallback)
            setSessionToken(sessionToken)
            isActive = true
        }

        serviceScope.launch {
            firebaseMusicSource.fetchMediaData()
            firebaseArtistSource.fetchArtistData()
            firebaseGenreSource.fetchMediaData()
        }

    }

    private val mediaSessionCallback = object : MediaSessionCompat.Callback() {

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            super.onPlayFromMediaId(mediaId, extras)

            if (firebaseMusicSource.songs.isNotEmpty()) {
                val song = firebaseMusicSource.songs.find {
                    it.description.mediaId == mediaId
                }
                startPlaying(song!!)

            }
        }

        override fun onPause() {
            exoPlayer.pause()
        }

        override fun onStop() {
            exoPlayer.stop()
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        // Provide the root of the media tree for browsing
        return BrowserRoot(MEDIA_ROOT_ID, null)
    }

    //*no data showing [firebaseMusicSource.asMediaItems()]
    /* 1.Ensure fetchMediaData() is called before onLoadChildren.
       2.Verify that the fetched songs are not empty.
       3.Log onLoadChildren calls to ensure the correct parentId is used.
       4.Use result.detach() and sendResult() after loading data asynchronously.
       5.Make sure MusicDatabase is returning the expected data.*/

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        Timber.tag("onLoadChildren").d("parentId: %s", parentId)
        result.detach()
        when(parentId) {

            MEDIA_SONG_ID -> {
                serviceScope.launch {
                    // Ensure that the data is fetched before proceeding
                    if (firebaseMusicSource.songs.isEmpty()) {
                        firebaseMusicSource.fetchMediaData()
                    }
                    val mediaItems = firebaseMusicSource.asMediaItems()
                    // Send the result after data is ready
                    result.sendResult(mediaItems.toMutableList())
                }

            }

            MEDIA_ARTIST_ID -> {
                serviceScope.launch {
                    // Ensure that the data is fetched before proceeding
                    if (firebaseArtistSource.artists.isEmpty()) {
                        firebaseArtistSource.fetchArtistData()
                    }
                    val mediaItems = firebaseArtistSource.asMediaItems()
                    // Send the result after data is ready
                    result.sendResult(mediaItems.toMutableList())
                }
            }

            MEDIA_GENRE_ID -> {
                serviceScope.launch {
                    // Ensure that the data is fetched before proceeding
                    if (firebaseGenreSource.genres.isEmpty()) {
                        firebaseGenreSource.fetchMediaData()
                    }
                    val mediaItems = firebaseGenreSource.asMediaItems()
                    // Send the result after data is ready
                    result.sendResult(mediaItems.toMutableList())
                }
            }
        }
    }

    fun startPlaying(mediaItem: MediaMetadataCompat) {
        playSong(mediaItem)
        exoPlayer.apply {
            setMediaItem(asMediaItemExo(mediaItem))
            playWhenReady = true
            prepare() // Prepares the media to be played
            play() // Starts playback
        }
    }

    private fun playSong(song: MediaMetadataCompat) {
        mediaSession.setMetadata(song)

        val playbackState = PlaybackStateCompat.Builder()
            .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1f)
            .build()
        mediaSession.setPlaybackState(playbackState)

    }

    private fun asMediaItemExo(song: MediaMetadataCompat): MediaItem {
        return MediaItem.fromUri(song.description.mediaUri.toString())
    }


}