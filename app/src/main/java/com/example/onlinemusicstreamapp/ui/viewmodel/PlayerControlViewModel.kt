package com.example.onlinemusicstreamapp.ui.viewmodel

import android.support.v4.media.session.PlaybackStateCompat.*
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.exoplayer.service.music.MusicServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerControlViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection
): ViewModel() {

    private var _song = MutableLiveData<Song>()
    val song: LiveData<Song> = _song

    val playbackState = musicServiceConnection.playbackState

    val currentPlayingSong = musicServiceConnection.currentPlayingSong

    init {
        skipToNextSongIfTheCurrentSongEnd()

    }

    fun play(song: Song) {
        Log.d("playbackStateInViewModel", "${playbackState.value}")
        musicServiceConnection.transportControls.playFromMediaId(song.mediaId, null)
        song.let { _song.postValue(it) }

    }

    fun togglePlayPause() {
        val currentState = playbackState.value?.state
        val currentPosition = playbackState.value?.position
        if (currentState != null) {
            Log.d("PosplayingSong", "$currentPosition and ${song.value?.title}")
            when (currentState) {
                STATE_PLAYING -> {
                    musicServiceConnection.transportControls.pause()
                }
                STATE_PAUSED, STATE_STOPPED -> {
                    musicServiceConnection.transportControls.play()
                }
                else -> {
                    Log.d("togglePlayPause", "Unhandled playback state: $currentState")
                }
            }
        }
    }

    fun skipToNext() {
        musicServiceConnection.transportControls.skipToNext()
    }

    fun skipToPrevious() {
        musicServiceConnection.transportControls.skipToPrevious()
    }

    fun stop() {
        musicServiceConnection.transportControls.stop()
    }

    fun rewind() {
        musicServiceConnection.transportControls.rewind()
    }

    fun seekTo(pos: Long) {
        musicServiceConnection.transportControls.seekTo(pos)
    }

    private fun skipToNextSongIfTheCurrentSongEnd() {
        musicServiceConnection.playbackState.observeForever { playbackState ->
            if (playbackState?.state == STATE_STOPPED) {
                skipToNext()
            }
        }
    }


}