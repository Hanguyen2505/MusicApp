package com.example.onlinemusicstreamapp.database.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val mediaId: String =  "",
    val title: String= "",
    val artist: List<String> = emptyList(),
    val songUrl: String = "",
    val imageUrl: String = "",
    val genre: List<String> = emptyList()
): Parcelable
