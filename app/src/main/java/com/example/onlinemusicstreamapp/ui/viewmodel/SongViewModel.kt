package com.example.onlinemusicstreamapp.ui.viewmodel

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinemusicstreamapp.database.data.remote.MusicDatabase
import com.example.onlinemusicstreamapp.database.data.entities.Artist
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.exoplayer.MusicServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

    val songs = MutableLiveData<List<Song>>()

    val artist = MutableLiveData<List<Artist>>()

    private val searchMediaItems = MutableLiveData<List<Song>>()

    private val musicDatabase = MusicDatabase()


    init {
        getAllSongs()
    }

    private fun fetchSongsFromMediaBrowser(
        parentId: String,
        callback: (List<Song>) -> Unit
    ) {
        musicServiceConnection.subscribe(parentId, object : MediaBrowserCompat.SubscriptionCallback() {
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                val songs = children.map {
                    Song(
                        it.mediaId!!,
                        it.description.title.toString(),
                        it.description.subtitle.toString().split(","),
                        it.description.mediaUri.toString(),
                        it.description.iconUri.toString()
                    )
                }
                callback(songs)
            }
        })
    }

    private fun getAllSongs(): MutableLiveData<List<Song>> {
        fetchSongsFromMediaBrowser("root_id") { items ->
            songs.postValue(items)
        }
        return songs
    }

    fun searchMusic(searchQuery: String): MutableLiveData<List<Song>> {
        fetchSongsFromMediaBrowser("root_id") { searchItems ->
            val filteredSongs = searchItems.filter { it.title.contains(searchQuery, ignoreCase = true) }
            searchMediaItems.postValue(filteredSongs)
        }
        return searchMediaItems
    }
}