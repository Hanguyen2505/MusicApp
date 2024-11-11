package com.example.onlinemusicstreamapp.database.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val playlistId: String = "",
    val title: String = "", //collection id
    val description: String = "",
    val songIds: List<String> = emptyList(),
    val coverImageUrl: String = ""
): Parcelable