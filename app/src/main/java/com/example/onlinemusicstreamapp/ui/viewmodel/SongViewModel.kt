package com.example.onlinemusicstreamapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onlinemusicstreamapp.database.data.entities.Artist
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_SONG_ID
import com.example.onlinemusicstreamapp.exoplayer.callbacks.MyMediaBrowser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val myMediaBrowser: MyMediaBrowser
) : ViewModel() {

    val songs = MutableLiveData<List<Song>>()

    val artist = MutableLiveData<List<Artist>>()

    private val searchMediaItems = MutableLiveData<List<Song>>()



    init {
        getAllSongs()
    }


    private fun getAllSongs(): MutableLiveData<List<Song>> {
        myMediaBrowser.fetchSongsFromMediaBrowser(MEDIA_SONG_ID) { items ->
            songs.postValue(items)
        }
        return songs
    }

    fun searchMusic(searchQuery: String): MutableLiveData<List<Song>> {
        myMediaBrowser.fetchSongsFromMediaBrowser(MEDIA_SONG_ID) { searchItems ->
            val filteredSongs = searchItems.filter { it.title.contains(searchQuery, ignoreCase = true) }
            searchMediaItems.postValue(filteredSongs)
        }
        return searchMediaItems
    }
}