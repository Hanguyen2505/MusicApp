package com.example.onlinemusicstreamapp.ui.viewmodel

import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.exoplayer.service.music.MusicServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class PlayerControlViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection
): ViewModel() {

    val playBackState = musicServiceConnection.playbackState

    val currentPlayingSong = musicServiceConnection.currentPlayingSong

    fun play(song: Song) {
        Log.d("playbackStateInViewModel", "${playBackState.value}")
        musicServiceConnection.transportControls.playFromMediaId(song.mediaId, null)

    }

    fun togglePlayPause() {
        val currentState = playBackState.value?.state
        if (currentState != null) {
            Log.d("playbackStateInViewModel", "$currentState and ${currentPlayingSong?.value?.description?.title}")

            when (currentState) {
                PlaybackStateCompat.STATE_PLAYING -> {
                    musicServiceConnection.transportControls.pause()
                }
                PlaybackStateCompat.STATE_PAUSED, PlaybackStateCompat.STATE_STOPPED -> {
                    musicServiceConnection.transportControls.play()
                }
                else -> {
                    Log.d("togglePlayPause", "Unhandled playback state: $currentState")
                }
            }
        }
    }

    fun stop() {
        musicServiceConnection.transportControls.stop()
    }

    fun rewind() {
        musicServiceConnection.transportControls.rewind()
    }


}