package com.example.onlinemusicstreamapp.database.data.entities

data class PersonalizedPlaylist(
    val id: String,
    val title: String,
    val description: String,
    val coverUrl: String,
    val songIds: List<String>,
    val userId: String
)
