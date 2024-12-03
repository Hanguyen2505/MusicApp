package com.example.onlinemusicstreamapp.database.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserPlaylist(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val coverUrl: String = "",
    val songIds: List<String> = emptyList(),
    val userId: String = "",
    val userName: String = "",
    val userPhotoUrl: String = ""
) : Parcelable
