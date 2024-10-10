package com.example.onlinemusicstreamapp

import android.util.Log
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class SomeClass {
    val data = Firebase.firestore
    val songs: List<String> = listOf("4xfKjeF7yNeblHPJwui9", "a5L4Wno1YqhZMqSeqBeT")

    init {
        getData()
    }

    fun getData() {
        songs.forEach { song ->
            data.collection("songs").document(song).get().addOnSuccessListener {
                Log.d("test", "${it.data}")
            }
        }

    }
}