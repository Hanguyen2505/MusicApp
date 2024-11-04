package com.example.onlinemusicstreamapp.ui.viewmodel

import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.*
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.exoplayer.service.music.MusicService
import com.example.onlinemusicstreamapp.exoplayer.service.music.MusicServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class PlayerControlViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection
): ViewModel() {

    //Used to update UI with current song
    private var _song = MutableLiveData<Song>()
    val song: LiveData<Song> = _song

    val playBackState = musicServiceConnection.playbackState

    val currentPlayingSong = musicServiceConnection.currentPlayingSong

    private var _songDuration: MutableLiveData<Long> = MutableLiveData()
    val songDuration: LiveData<Long> = _songDuration

    fun play(song: Song) {
        Log.d("playbackStateInViewModel", "${playBackState.value}")
        musicServiceConnection.transportControls.playFromMediaId(song.mediaId, null)
        song.let { _song.postValue(it) }

    }

    fun togglePlayPause() {
        val currentState = playBackState.value?.state
        val currentPosition = playBackState.value?.position
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

    fun stop() {
        musicServiceConnection.transportControls.stop()
    }

    fun rewind() {
        musicServiceConnection.transportControls.rewind()
    }

    fun seekTo(pos: Long) {
        musicServiceConnection.transportControls.seekTo(pos)
    }


}