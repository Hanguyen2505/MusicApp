package com.example.onlinemusicstreamapp.database.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    val genreId: String = "",
    val name: String = "",
    val genreImg: String = "",
    val color: String = ""
): Parcelable