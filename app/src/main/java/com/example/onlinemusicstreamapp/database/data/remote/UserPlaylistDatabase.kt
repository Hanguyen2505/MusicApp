package com.example.onlinemusicstreamapp.database.data.remote

import android.util.Log
import com.example.onlinemusicstreamapp.database.data.entities.UserPlaylist
import com.example.onlinemusicstreamapp.database.other.Constants.USER_PLAYLIST_COLLECTION
import com.example.onlinemusicstreamapp.database.repository.UserAuthorization
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserPlaylistDatabase @Inject constructor(
    private val userAuthorization: UserAuthorization
) {

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
    fun subscribeToRealtimeUpdates(onResult: (List<UserPlaylist>) -> Unit) {
        userPlaylistCollection.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                Log.d("userData", "error: $it")
                return@addSnapshotListener
            }
            querySnapshot?.let {
                val filterPlaylist = it.toObjects<UserPlaylist>().filter { playlist ->
                    playlist.userId == userAuthorization.getCurrentUserId()
                }
                onResult(filterPlaylist)
            }
        }
    }

    suspend fun getUserPlaylist(): List<UserPlaylist> {
        return try {
            userPlaylistCollection.get().await().toObjects<UserPlaylist>().filter {
                it.userId == userAuthorization.getCurrentUserId()
            }
        }
        catch (e: Exception) {
            Log.d("userPlaylist", "error: $e")
            emptyList()
        }

    }

}