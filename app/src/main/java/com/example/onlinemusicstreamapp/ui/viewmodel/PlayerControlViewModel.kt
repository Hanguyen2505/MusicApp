package com.example.onlinemusicstreamapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.exoplayer.service.music.MusicServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerControlViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection
): ViewModel() {
    fun play(song: Song) {
        musicServiceConnection.transportControls.playFromMediaId(song.mediaId, null)
    }


}