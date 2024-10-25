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

}