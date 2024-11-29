package com.example.onlinemusicstreamapp.database.data.remote

import com.example.onlinemusicstreamapp.database.data.entities.UserPlaylist
import com.example.onlinemusicstreamapp.database.other.Constants.USER_PLAYLIST_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserPlaylistDatabase {

    private val fireStore = FirebaseFirestore.getInstance()

    private val userPlaylistCollection = fireStore.collection(USER_PLAYLIST_COLLECTION)

    suspend fun createPlaylist(userPlaylist: UserPlaylist) {
        try {
            userPlaylistCollection.add(userPlaylist).addOnSuccessListener {
                println("Playlist created successfully")
            }.addOnFailureListener {
                println("Failed to create playlist")
            }.await()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

}