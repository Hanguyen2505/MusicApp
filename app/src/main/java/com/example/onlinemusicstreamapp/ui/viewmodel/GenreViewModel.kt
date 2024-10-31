package com.example.onlinemusicstreamapp.ui.viewmodel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onlinemusicstreamapp.database.data.remote.GenreDatabase
import com.example.onlinemusicstreamapp.database.data.entities.Genre
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_GENRE_ID
import com.example.onlinemusicstreamapp.database.other.Constants.MEDIA_SONG_ID
import com.example.onlinemusicstreamapp.exoplayer.callbacks.MyMediaFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val myMediaFactory: MyMediaFactory
): ViewModel() {
    private val genreDatabase = GenreDatabase()
    val songs = MutableLiveData<List<Song>>()
    val genre = MutableLiveData<List<Genre>>()

    init {
        getAllGenres()
    }

    private fun getAllGenres(): MutableLiveData<List<Genre>> {
        myMediaFactory.fetchGenreFromMediaBrowser(MEDIA_GENRE_ID) { items ->
            genre.postValue(items)
        }
        return genre
    }

    fun filterSongsByGenre(genre: String): MutableLiveData<List<Song>> {
        myMediaFactory.fetchSongsFromMediaBrowser(MEDIA_SONG_ID) { items ->
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