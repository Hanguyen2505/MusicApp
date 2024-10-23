package com.example.onlinemusicstreamapp.database.data.entities


data class Song(
    val mediaId: String =  "",
    val title: String= "",
    val artist: List<String> = emptyList(),
    val songUrl: String = "",
    val imageUrl: String = "",
    val genre: List<String> = emptyList()
)
