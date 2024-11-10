package com.example.onlinemusicstreamapp.database.data.entities

data class Playlist(
    val playlistId: String = "",
    val title: String = "", //collection id
    val description: String = "",
    val songIds: List<String> = emptyList(),
    val coverImageUrl: String = ""
    )