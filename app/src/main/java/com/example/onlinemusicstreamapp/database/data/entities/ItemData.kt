package com.example.onlinemusicstreamapp.database.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class ItemData : Parcelable {
    @Parcelize
    data class ArtistItem(val artist: Artist) : ItemData()

    @Parcelize
    data class PlaylistItem(val playlist: Playlist) : ItemData()
}