package com.example.onlinemusicstreamapp.database.data.entities

import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Artist (
    val name: String = "",
    val imageUrl: String = "",
    val songs: List<String> = emptyList(),
//    val songRef : @RawValue DocumentReference
): Parcelable