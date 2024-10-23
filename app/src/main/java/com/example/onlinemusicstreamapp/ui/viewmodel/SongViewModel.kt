package com.example.onlinemusicstreamapp.ui.viewmodel

import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinemusicstreamapp.database.data.remote.MusicDatabase
import com.example.onlinemusicstreamapp.database.data.entities.Artist
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.exoplayer.MusicService
import com.example.onlinemusicstreamapp.exoplayer.MusicServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

    val songs = MutableLiveData<List<Song>>()

    val artist = MutableLiveData<List<Artist>>()

    private val search = MutableLiveData<List<Song>>()

    private val musicDatabase = MusicDatabase()


    init {
        getAllSongs()
    }

    private fun getAllSongs() {
//        viewModelScope.launch(Dispatchers.IO) {
//            songs.postValue(musicDatabase.getAllSongs())
//            Log.d("SONGTAG", "${musicDatabase.getAllSongs()}")
//        }

        musicServiceConnection.subscribe("root_id", object : MediaBrowserCompat.SubscriptionCallback() {
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                super.onChildrenLoaded(parentId, children)
                val items = children.map {
                    Song(
                        it.mediaId!!,
                        it.description.title.toString(),
                        it.description.subtitle.toString().split(","),
                        it.description.mediaUri.toString(),
                        it.description.iconUri.toString()
                    )
                }
                songs.postValue(items)
            }
        })
    }

    fun searchMusic(searchQuery: String): LiveData<List<Song>> {
        viewModelScope.launch(Dispatchers.IO) {
            search.postValue(musicDatabase.searchSong(searchQuery))
        }
        return search
    }
}