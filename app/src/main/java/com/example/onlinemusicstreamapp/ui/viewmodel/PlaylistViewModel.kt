package com.example.onlinemusicstreamapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onlinemusicstreamapp.database.data.entities.Playlist
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_PLAYLIST_ID
import com.example.onlinemusicstreamapp.exoplayer.callbacks.MyMediaFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val mediaFactory: MyMediaFactory
) : ViewModel() {
    private val _playlist = MutableLiveData<List<Playlist>>()
    val playlist: LiveData<List<Playlist>> = _playlist

    init {
        getAllPlaylists()
    }

    private fun getAllPlaylists() {
        mediaFactory.fetchPlaylistFromMediaBrowser(MEDIA_PLAYLIST_ID) { playlists ->
            Log.d("PlaylistViewModel", "getAllPlaylists: $playlists")
            _playlist.postValue(playlists)
        }
    }
}