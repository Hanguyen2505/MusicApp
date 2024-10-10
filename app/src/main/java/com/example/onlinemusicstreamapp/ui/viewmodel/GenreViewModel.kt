package com.example.onlinemusicstreamapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinemusicstreamapp.database.data.remote.GenreDatabase
import com.example.onlinemusicstreamapp.database.data.entities.Genre
import com.example.onlinemusicstreamapp.database.data.entities.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GenreViewModel: ViewModel() {
    private val genreDatabase = GenreDatabase()
    val song = MutableLiveData<List<Song>>()
    val genre = MutableLiveData<List<Genre>>()

    init {
        getGenre()
    }

    private fun getGenre() {
        viewModelScope.launch(Dispatchers.IO) {
            genre.postValue(genreDatabase.getAllGenre())

        }
    }

    fun searchSong(genre: String): LiveData<List<Song>> {
        viewModelScope.launch(Dispatchers.IO) {
            song.postValue(genreDatabase.fetchSong(genre))
        }
        return song
    }
}