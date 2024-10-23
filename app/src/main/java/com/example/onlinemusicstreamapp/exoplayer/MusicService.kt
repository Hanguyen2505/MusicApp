package com.example.onlinemusicstreamapp.exoplayer

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.database.data.remote.MusicDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MusicService: MediaBrowserServiceCompat() {

    private val SERVICE_TAG = "MusicService"

    //    private var exoPlayer: ExoPlayer? = null
    private var currentSong: Song? = null

    private lateinit var mediaSession: MediaSessionCompat

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var firebaseMusicSource: FirebaseMusicSource

    override fun onCreate() {
        super.onCreate()

        // Set up MediaSession
        mediaSession = MediaSessionCompat(this, SERVICE_TAG)
        // Set the session token so that clients can connect to the session
        sessionToken = mediaSession.sessionToken

        mediaSessionCallback

        serviceScope.launch {
            firebaseMusicSource.fetchMediaData()
        }

    }

    private val mediaSessionCallback = object : MediaSessionCompat.Callback() {
        override fun onPlay() {
            // Handle play action
            mediaSession.isActive = true  // Activate the session
            // Start playing media

            exoPlayer.playWhenReady = true
        }

        override fun onPause() {
            // Handle pause action
            // Pause media playback
            exoPlayer.pause()
        }

        override fun onStop() {
            exoPlayer.stop()
        }

        // Implement other media control actions like stop, skip, etc.
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        // Provide the root of the media tree for browsing
        return BrowserRoot("root_id", null)
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
        Log.d("onLoadChildren", "parentId: $parentId")
        result.detach()
        when(parentId) {

            "root_id" -> {
                serviceScope.launch {
                    // Ensure that the data is fetched before proceeding
                    if (firebaseMusicSource.songs.isEmpty()) {
                        firebaseMusicSource.fetchMediaData()
                    }
                    val mediaItems = firebaseMusicSource.asMediaItems()
                    Log.d("musicService", "Loaded MediaItems: $mediaItems")

                    // Send the result after data is ready
                    result.sendResult(mediaItems.toMutableList())
                }

            }
        }
    }


    fun startPlaying(context: Context, song: Song) {
        if (currentSong != song) {
            currentSong = song
            currentSong?.songUrl?.apply {
                Log.d("currentSong", "$this")
                val mediaItem = MediaItem.fromUri(Uri.parse(this))
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.playWhenReady = true
                exoPlayer.prepare()
                exoPlayer.play()
            }

        }
    }

}