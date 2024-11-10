package com.example.onlinemusicstreamapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onlinemusicstreamapp.database.data.entities.Artist
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_SONG_ID
import com.example.onlinemusicstreamapp.exoplayer.callbacks.MyMediaFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val myMediaFactory: MyMediaFactory
) : ViewModel() {

    val songs = MutableLiveData<List<Song>>()

    val artist = MutableLiveData<List<Artist>>()

    private val searchMediaItems = MutableLiveData<List<Song>>()



    init {
        getAllSongs()
    }


    private fun getAllSongs(): MutableLiveData<List<Song>> {
        myMediaFactory.fetchSongsFromMediaBrowser(MEDIA_SONG_ID) { items ->
            songs.postValue(items)
        }
        return songs
    }

    fun searchMusic(searchQuery: String): MutableLiveData<List<Song>> {
        myMediaFactory.fetchSongsFromMediaBrowser(MEDIA_SONG_ID) { searchItems ->
            val filteredSongs = searchItems.filter { it.title.contains(searchQuery, ignoreCase = true) }
            searchMediaItems.postValue(filteredSongs)
        }
        return searchMediaItems
    }
}