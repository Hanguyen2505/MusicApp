package com.example.onlinemusicstreamapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinemusicstreamapp.database.data.entities.UserPlaylist
import com.example.onlinemusicstreamapp.database.data.entities.Playlist
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.database.data.remote.UserPlaylistDatabase
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_PLAYLIST_ID
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_SONG_ID
import com.example.onlinemusicstreamapp.exoplayer.callbacks.MyMediaFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val mediaFactory: MyMediaFactory,
) : ViewModel() {
    private val _playlist = MutableLiveData<List<Playlist>>()
    val playlist: LiveData<List<Playlist>> = _playlist

    private val _songsInPlaylist = MutableLiveData<List<Song>>()

    init {
        getAllPlaylists()
    }

    private fun getAllPlaylists() = viewModelScope.launch {
        mediaFactory.fetchPlaylistFromMediaBrowser(MEDIA_PLAYLIST_ID) { playlists ->
            _playlist.postValue(playlists)
        }
    }

    fun getSongsInPlaylist(playlistId: String): MutableLiveData<List<Song>> {

        mediaFactory.fetchPlaylistFromMediaBrowser(MEDIA_PLAYLIST_ID) { playlists ->
            playlists.find { playlist ->
                playlist.playlistId == playlistId
            }?.let {
                mediaFactory.fetchSongsFromMediaBrowser(MEDIA_SONG_ID) { songs ->
                    songs.filter { song ->
                        song.mediaId in it.songIds
                    }.let {
                        _songsInPlaylist.postValue(it)
                    }
                }
            }

        }
        return _songsInPlaylist
    }

}