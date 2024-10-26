package com.example.onlinemusicstreamapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinemusicstreamapp.database.data.remote.GenreDatabase
import com.example.onlinemusicstreamapp.database.data.entities.Genre
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_GENRE_ID
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_SONG_ID
import com.example.onlinemusicstreamapp.exoplayer.callbacks.MyMediaBrowser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val myMediaBrowser: MyMediaBrowser
): ViewModel() {
    private val genreDatabase = GenreDatabase()
    val songs = MutableLiveData<List<Song>>()
    val genre = MutableLiveData<List<Genre>>()

    init {
        getAllGenres()
    }

    private fun getAllGenres(): MutableLiveData<List<Genre>> {
        myMediaBrowser.fetchGenreFromMediaBrowser(MEDIA_GENRE_ID) { items ->
            genre.postValue(items)
        }
        return genre
    }

    fun filterSongsByGenre(genre: String): MutableLiveData<List<Song>> {
        myMediaBrowser.fetchSongsFromMediaBrowser(MEDIA_SONG_ID) { items ->
            val filteredSongs = items.filter { song ->
                song.genre.any {
                    it.contains(genre, ignoreCase = true)
                }
            }
            songs.postValue(filteredSongs)
        }
        return songs
    }
}