package com.example.onlinemusicstreamapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinemusicstreamapp.database.data.remote.ArtistDatabase
import com.example.onlinemusicstreamapp.database.data.remote.MusicDatabase
import com.example.onlinemusicstreamapp.database.data.entities.Artist
import com.example.onlinemusicstreamapp.database.data.entities.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtistViewModel: ViewModel() {
    val music = MutableLiveData<List<Song>>()
    val artist = MutableLiveData<List<Artist>>()
    private val search = MutableLiveData<List<Artist>>()
    private val musicDatabase = MusicDatabase()
    private val artistDatabase = ArtistDatabase()

    init {
        getArtists()
        getAlbum()
    }

    private fun getAlbum() {
        viewModelScope.launch(Dispatchers.IO) {
            music.postValue(musicDatabase.getAlbumSong())
        }
    }

    private fun getArtists() {
        viewModelScope.launch(Dispatchers.IO) {
            artist.postValue(artistDatabase.getAllArtist())
        }
    }

    fun getSong(artist: String): LiveData<List<Song>> {
        viewModelScope.launch(Dispatchers.IO) {
            music.postValue(artistDatabase.fetchSong(artist))
        }
        Log.d("songlistLiveData", "${music.value}")
        return music
    }

    fun searchArtist(searchQuery: String): LiveData<List<Artist>> {
        viewModelScope.launch(Dispatchers.IO) {
            search.postValue(artistDatabase.searchArtist(searchQuery))
        }
        return search
    }
}