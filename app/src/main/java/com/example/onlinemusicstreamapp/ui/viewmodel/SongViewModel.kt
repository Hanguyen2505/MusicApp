package com.example.onlinemusicstreamapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinemusicstreamapp.database.data.remote.MusicDatabase
import com.example.onlinemusicstreamapp.database.data.entities.Artist
import com.example.onlinemusicstreamapp.database.data.entities.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SongViewModel @Inject constructor(

) : ViewModel() {
    val songs = MutableLiveData<List<Song>>()
    val artist = MutableLiveData<List<Artist>>()
    private val search = MutableLiveData<List<Song>>()
    private val musicDatabase = MusicDatabase()

    init {
        getAllSongs()

    }

    private fun getAllSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            songs.postValue(musicDatabase.getAllSong())
            Log.d("SONGTAG", "${musicDatabase.getAllSong()}")
        }

    }

    fun searchMusic(searchQuery: String): LiveData<List<Song>> {
        viewModelScope.launch(Dispatchers.IO) {
            search.postValue(musicDatabase.searchSong(searchQuery))
        }
        return search
    }
}