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

    suspend fun updatePlaylist(userPlaylist: UserPlaylist) {
        try {
            val map = mapOf(
                "title" to userPlaylist.title,
                "description" to userPlaylist.description,
                "coverUrl" to userPlaylist.coverUrl
            )
            userPlaylistCollection.document(userPlaylist.id).update(map)
                .addOnSuccessListener {
                    println("Playlist updated successfully")
                }.addOnFailureListener {
                    println("Failed to update playlist")
                }.await()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getPlaylistBySongId(songId: String): List<UserPlaylist> {
        return try {
            val filterPlaylist =
                userPlaylistCollection.whereArrayContains("songIds", songId).get().await()
            filterPlaylist.toObjects<UserPlaylist>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun deletePlaylist(playlistId: String) {
        try {
            userPlaylistCollection.document(playlistId).delete()
                .addOnSuccessListener {
                    println("Playlist deleted successfully")
                }.addOnFailureListener {
                    println("Failed to delete playlist")
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
        userPlaylistCollection.document(playlistId).update(
            "songIds",
            FieldValue.arrayUnion(songId)
        ).await()
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

    private suspend fun getPlaylist(playlistId: String): UserPlaylist? {
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

    suspend fun subscribeToRealTimePlaylistUpdates(playlistId: String, onResult: (List<Song>) -> Unit) {
        getPlaylist(playlistId).let { playlist ->
            songCollection.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    Log.d("userData", "error: $it")
                    return@addSnapshotListener
                }
                querySnapshot?.let {
                    val songInPlaylist = it.toObjects<Song>().filter {  song ->
                        song.mediaId in playlist!!.songIds
                    }
                    onResult(songInPlaylist)
                }
            }
        }

    }

    suspend fun removeSongFromPlaylist(playlistId: String, songId: String) {
        userPlaylistCollection.document(playlistId).update(
            "songIds",
            FieldValue.arrayRemove(songId)
        ).addOnSuccessListener {
            Log.d("removeSongFromPlaylist", "Successfully removed $songId from $playlistId")
        }.addOnFailureListener {
            Log.d("removeSongFromPlaylist", "Failed to remove $songId from $playlistId")
        }.await()
    }

}