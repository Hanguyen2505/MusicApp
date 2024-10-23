package com.example.onlinemusicstreamapp.database.data.remote

import android.util.Log
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.database.repository.ArtistRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class MusicDatabase {
    private val fireStore = FirebaseFirestore.getInstance()
    private val songCollection = fireStore.collection("songs")
    private val songList = mutableListOf<Song>()
    private val songs = ArtistRepository.getSongs()

    suspend fun getAllSongs(): List<Song> {
        return try {
            songCollection.get().await().toObjects<Song>()
        }
        catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getAlbumSong(): List<Song> {
        songs.forEach {
            fireStore.document(it).get().await().toObject<Song>()
                ?.let { it1 ->
                    songList.add(it1)
                }
        }
        Log.d("database", "$songList")
        return songList
    }

    suspend fun searchSong(searchQuery: String): List<Song> {
        return try {
            // Step 1: Fetch a broad range of songs (you can refine the range if needed)
            val songsCol = songCollection.get().await().toObjects<Song>()

            // Step 2: Client-side filtering to find songs whose titles contain the search query
            songsCol.filter { it.title.contains(searchQuery, ignoreCase = true) }
        }
        catch (e: Exception) {
            emptyList()
        }
    }

}