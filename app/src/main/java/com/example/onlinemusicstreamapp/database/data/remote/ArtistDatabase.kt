package com.example.onlinemusicstreamapp.database.data.remote

import com.example.onlinemusicstreamapp.database.data.entities.Artist
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class ArtistDatabase {
    private val fireStore = FirebaseFirestore.getInstance()
    private val artistCollection = fireStore.collection("artist")
    private val songCollection = fireStore.collection("songs")

    suspend fun getAllArtist(): List<Artist> {
        return try {
            artistCollection.get().await().toObjects<Artist>()
        }
        catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun fetchSong(artist: String): List<Song> {
        return try {
            songCollection.whereArrayContains("artist", artist).get().await().toObjects<Song>()
        }
        catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun searchArtist(searchQuery: String): List<Artist> {
        return try {
            // Step 1: Fetch a broad range of songs (you can refine the range if needed)
            val artistCol = artistCollection.get().await().toObjects<Artist>()

            // Step 2: Client-side filtering to find songs whose titles contain the search query
            artistCol.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
        catch (e: Exception) {
            emptyList()
        }
    }
}