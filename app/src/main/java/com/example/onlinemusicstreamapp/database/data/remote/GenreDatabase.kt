package com.example.onlinemusicstreamapp.database.data.remote

import com.example.onlinemusicstreamapp.database.data.entities.Genre
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.database.other.Constants.GENRE_COLLECTION
import com.example.onlinemusicstreamapp.database.other.Constants.SONG_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class GenreDatabase {
    private val fireStore = FirebaseFirestore.getInstance()
    private val genreCollection = fireStore.collection(GENRE_COLLECTION)
    private val songCollection = fireStore.collection(SONG_COLLECTION)

    suspend fun getAllGenre(): List<Genre> {
        return try {
            genreCollection.get().await().toObjects<Genre>()
        }
        catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun fetchSong(genre: String): List<Song> {
        return try {
            songCollection.whereArrayContains("genre", genre).get().await().toObjects<Song>()
        }
        catch (e: Exception) {
            emptyList()
        }
    }

}