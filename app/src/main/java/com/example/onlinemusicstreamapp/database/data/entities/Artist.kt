package com.example.onlinemusicstreamapp.database.data.entities

import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Artist (
    val artistId: String = "",
    val name: String = "",
    val imageUrl: String = ""
): Parcelable