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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPlaylistViewModel @Inject constructor(
    private val userPlaylistDatabase: UserPlaylistDatabase,
) : ViewModel() {
    private val _playlist = MutableLiveData<List<Playlist>>()
    val playlist: LiveData<List<Playlist>> = _playlist

    private val _userPlaylists = MutableLiveData<List<UserPlaylist>>()
    val userPlaylists: LiveData<List<UserPlaylist>> = _userPlaylists

    private val _songsInPlaylist = MutableLiveData<List<Song>>()
    val songsInPlaylist: LiveData<List<Song>> get() = _songsInPlaylist

    //TODO bring getSongInPlaylist to init function
    init {
        getRealTimeUserPlaylist()
    }

    private fun getRealTimeUserPlaylist() = viewModelScope.launch {
        userPlaylistDatabase.subscribeToRealtimeUpdates { playlists ->
            _userPlaylists.postValue(playlists)
        }
    }

    fun getSongsInUserPlaylist(userPlaylistId: String): LiveData<List<Song>> {
        viewModelScope.launch(Dispatchers.IO) {
            userPlaylistDatabase.subscribeToRealTimePlaylistUpdates(userPlaylistId) { songs ->
                Log.d("songsInPlaylist", "Updated songs: $songs")
                _songsInPlaylist.postValue(songs)
            }
        }
        return _songsInPlaylist
    }

    fun createPlaylist(userPlaylist: UserPlaylist) = viewModelScope.launch {
        try {
            userPlaylistDatabase.createPlaylist(userPlaylist)
        }
        catch(e: Exception) {
            Log.d("uploadData", "error: $e")
        }
    }

    fun addSongToPlaylist(playlistId: String, songId: String) = viewModelScope.launch(Dispatchers.IO) {
        userPlaylistDatabase.addSongToPlaylist(playlistId, songId)
        
    }

    fun updatePlaylist(userPlaylist: UserPlaylist) = viewModelScope.launch(Dispatchers.IO) {
        userPlaylistDatabase.updatePlaylist(userPlaylist)
    }

    fun deleteUserPlaylist(playlistId: String) = viewModelScope.launch(Dispatchers.IO) {
        userPlaylistDatabase.deletePlaylist(playlistId)

    }

}