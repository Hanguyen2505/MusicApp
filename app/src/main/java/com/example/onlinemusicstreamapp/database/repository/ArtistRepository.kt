package com.example.onlinemusicstreamapp.database.repository

import android.util.Log
import com.example.onlinemusicstreamapp.database.data.entities.Artist
import com.google.firebase.firestore.CollectionReference

object ArtistRepository {

    private var songs = emptyList<String>()
    private var currentArtist: Artist? = null

    fun getSongs(): List<String> {
        return songs
    }

    fun getCurrentArtist(): Artist? {
        return currentArtist
    }

    fun getData(artist: Artist) {
        Log.d("repository", "$artist")
        currentArtist = artist
        Log.d("repository", "$songs")
    }

}