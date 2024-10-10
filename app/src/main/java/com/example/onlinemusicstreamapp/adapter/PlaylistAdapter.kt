package com.example.onlinemusicstreamapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.database.data.entities.Playlist
import com.example.onlinemusicstreamapp.databinding.CardviewPlaylistBinding
import com.example.onlinemusicstreamapp.databinding.FragmentLibraryBinding

class PlaylistAdapter (
    private val playlist: List<Playlist>
): RecyclerView.Adapter<PlaylistAdapter.MyViewHolder>() {

    class MyViewHolder(
        private val binding: CardviewPlaylistBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindData(playlist: Playlist) {
            binding.playlistTitle.text = playlist.title
            binding.desc.text = playlist.desc
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardviewPlaylistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return playlist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(playlist[position])


    }

}