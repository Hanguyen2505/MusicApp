package com.example.onlinemusicstreamapp.database.data.remote

import com.example.onlinemusicstreamapp.database.data.entities.Genre
import com.example.onlinemusicstreamapp.database.data.entities.Playlist
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.database.other.Constants.GENRE_COLLECTION
import com.example.onlinemusicstreamapp.database.other.Constants.PLAYLIST_COLLECTION
import com.example.onlinemusicstreamapp.database.other.Constants.SONG_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class PlaylistDatabase {
    private val fireStore = FirebaseFirestore.getInstance()
    private val playlistCollection = fireStore.collection(PLAYLIST_COLLECTION)
    private val songCollection = fireStore.collection(SONG_COLLECTION)

    suspend fun getAllPlaylists(): List<Playlist> {
        return try {
            playlistCollection.get().await().toObjects<Playlist>()
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