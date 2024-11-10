package com.example.onlinemusicstreamapp.database.data.remote

import com.example.onlinemusicstreamapp.database.data.entities.Artist
import com.example.onlinemusicstreamapp.database.other.Constants.ARTIST_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class ArtistDatabase {
    private val fireStore = FirebaseFirestore.getInstance()
    private val artistCollection = fireStore.collection(ARTIST_COLLECTION)

    suspend fun getAllArtist(): List<Artist> {
        return try {
            artistCollection.get().await().toObjects<Artist>()
        }
        catch (e: Exception) {
            emptyList()
        }
    }
}