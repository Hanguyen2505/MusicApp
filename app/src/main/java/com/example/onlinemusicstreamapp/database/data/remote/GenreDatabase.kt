package com.example.onlinemusicstreamapp.database.data.remote

import com.example.onlinemusicstreamapp.database.data.entities.Genre
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class GenreDatabase {
    private val fireStore = FirebaseFirestore.getInstance()
    private val genreCollection = fireStore.collection("genre")
    private val songCollection = fireStore.collection("songs")
    val string = "Gaming"

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