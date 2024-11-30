package com.example.onlinemusicstreamapp.database.data.entities

data class UserPlaylist(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val coverUrl: String = "",
    val songIds: List<String> = emptyList(),
    val userId: String = ""
)
