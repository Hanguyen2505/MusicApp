package com.example.onlinemusicstreamapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LibraryViewModel(application: Application): AndroidViewModel(application)  {
    private val playlist = Firebase.firestore

    //TODO create a playlist in library
    fun createPlaylist (
        playlistData: HashMap<String, String>,
        collectionPath: String
    ) = CoroutineScope(Dispatchers.IO).launch {
        playlist.collection(collectionPath).add(playlistData)
    }

    //TODO get all song data from firestore
    fun getAllSong () = CoroutineScope(Dispatchers.IO).launch {

        // * convert song in firestore into song data class  /
        playlist.collection("songs").get()
            .addOnSuccessListener {
            val songList = it.toObjects<Song>()
        }
    }

}