package com.example.onlinemusicstreamapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onlinemusicstreamapp.database.data.entities.Artist
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_ARTIST_ID
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_SONG_ID
import com.example.onlinemusicstreamapp.exoplayer.callbacks.MyMediaBrowser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val myMediaBrowser: MyMediaBrowser
): ViewModel() {
    val music = MutableLiveData<List<Song>>()
    val artist = MutableLiveData<List<Artist>>()
    private val searchArtist = MutableLiveData<List<Artist>>()

    init {
        getAllArtists()
    }


    private fun getAllArtists(): MutableLiveData<List<Artist>> {
        myMediaBrowser.fetchArtistFromMediaBrowser(MEDIA_ARTIST_ID) { items ->
            artist.postValue(items)
        }
        return artist
    }

    fun getSong(artist: String): MutableLiveData<List<Song>> {
        myMediaBrowser.fetchSongsFromMediaBrowser(MEDIA_SONG_ID) { items ->
            val filteredSongs = items.filter { song ->
                song.artist.any {
                    it.contains(artist, ignoreCase = true)
                }
            }
            music.postValue(filteredSongs)
        }
        return music
    }

    fun filterSongsByArtist(searchQuery: String): MutableLiveData<List<Artist>> {
        myMediaBrowser.fetchArtistFromMediaBrowser(MEDIA_ARTIST_ID) { searchItems ->
            val filteredArtists = searchItems.filter { it.name.contains(searchQuery, ignoreCase = true) }
            searchArtist.postValue(filteredArtists)
        }
        return searchArtist
    }
}