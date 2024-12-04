package com.example.onlinemusicstreamapp.database.data.remote

import android.util.Log
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.database.data.entities.UserPlaylist
import com.example.onlinemusicstreamapp.database.other.Constants.SONG_COLLECTION
import com.example.onlinemusicstreamapp.database.other.Constants.USER_PLAYLIST_COLLECTION
import com.example.onlinemusicstreamapp.database.repository.UserAuthorization
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserPlaylistDatabase @Inject constructor(
    private val userAuthorization: UserAuthorization
) {
    private val fireStore = FirebaseFirestore.getInstance()

    private val userPlaylistCollection = fireStore.collection(USER_PLAYLIST_COLLECTION)

    private val songCollection = fireStore.collection(SONG_COLLECTION)

    suspend fun createPlaylist(userPlaylist: UserPlaylist) {
        try {
            userPlaylistCollection.document(userPlaylist.id).set(userPlaylist)
                .addOnSuccessListener {
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

    suspend fun addSongToPlaylist(playlistId: String, songId: String) {
        val currentPlaylist = getUserPlaylist().find { playlist ->
            playlist.id == playlistId
        }
        userPlaylistCollection.document(currentPlaylist!!.id).update(
            "songIds",
            FieldValue.arrayUnion(songId)
        )
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

    suspend fun getPlaylist(playlistId: String): UserPlaylist? {
        return try {
            getUserPlaylist().find { playlist ->
                playlist.id == playlistId
            }
        }
        catch (e: Exception) {
            Log.d("userPlaylist", "error: $e")
            UserPlaylist()
        }
    }

    fun subscribeToRealTimePlaylistUpdates(onResult: (List<Song>) -> Unit) {
        songCollection.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                Log.d("userData", "error: $it")
                return@addSnapshotListener
            }
            querySnapshot?.let {
                val songInPlaylist = it.toObjects<Song>()
                onResult(songInPlaylist)
            }
        }
    }

}