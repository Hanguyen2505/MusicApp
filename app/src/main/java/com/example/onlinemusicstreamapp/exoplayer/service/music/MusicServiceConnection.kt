package com.example.onlinemusicstreamapp.exoplayer.service.music

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MusicServiceConnection(
    context: Context
)  {

    private val _playbackState = MutableLiveData<PlaybackStateCompat?>()
    val playbackState: LiveData<PlaybackStateCompat?> = _playbackState

    private val _currentPlayingSong = MutableLiveData<MediaMetadataCompat?>()
    val currentPlayingSong: LiveData<MediaMetadataCompat?> = _currentPlayingSong

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)

    //* mediaController: Once the MusicService is connected, this MediaControllerCompat allows the client to control the playback (e.g., play, pause, skip).
    lateinit var mediaController: MediaControllerCompat

    //* transportControls: Provides easy access to transport controls (play, pause, skip, etc.) through the MediaControllerCompat
    val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    //* handling the different states of connection and disconnection between the app and the *media service*
    private inner class MediaBrowserConnectionCallback(
        private val context: Context
    ) : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            Log.d("MusicServiceConnection", "CONNECTED")
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                registerCallback(MediaControllerCallback())
            }
        }

        override fun onConnectionSuspended() {
            Log.d("MusicServiceConnection", "SUSPENDED")

        }

        override fun onConnectionFailed() {
            Log.d("MusicServiceConnection", "FAILED")

        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            Log.d("playbackCurrentState", "PLAYBACK STATE CHANGED : $state")
            _playbackState.postValue(state)
            Log.d("playbackCurrentState", "PLAYBACK STATE CHANGED : ${state?.extras?.getLong("SONG_DURATION")}")
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            Log.d("playbackCurrentSong", "PLAYBACK STATE CHANGED : ${metadata!!.description.title}")
            _currentPlayingSong.postValue(metadata)

        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
            Log.d("playbackCurrentState", "PLAYBACK STATE CHANGED : ${event}")

        }

        override fun onSessionDestroyed() {
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }

    //* manage the connection between the app's components (like activities or fragments) and the MusicService
    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(
            context,
            MusicService::class.java
        ),
        mediaBrowserConnectionCallback,
        null
    ).apply { connect() }

    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

    init {
        Log.d("MusicServiceConnection", "${playbackState.value?.position}")
    }

}