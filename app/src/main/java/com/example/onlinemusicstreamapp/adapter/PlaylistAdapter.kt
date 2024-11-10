package com.example.onlinemusicstreamapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.database.data.entities.Artist
import com.example.onlinemusicstreamapp.database.data.entities.Playlist
import com.example.onlinemusicstreamapp.databinding.CardviewPlaylistBinding
import com.example.onlinemusicstreamapp.databinding.FragmentLibraryBinding

class PlaylistAdapter: RecyclerView.Adapter<PlaylistAdapter.MyViewHolder>() {

    var playlist = emptyList<Playlist>()

    private var onItemClickListener: ((Playlist) -> Unit)? = null

    fun setOnItemClickListener(listener: (Playlist) -> Unit) {
        onItemClickListener = listener
    }

    class MyViewHolder(
        private val binding: CardviewPlaylistBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindData(playlist: Playlist) {
            binding.title.text = playlist.title
            binding.desc.text = playlist.description
            Glide.with(binding.coverImg).load(playlist.coverImageUrl).into(binding.coverImg)
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

        val currentItem = playlist[position]
        holder.bindData(currentItem)

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { click ->
                click(currentItem)
            }
        }

    }

}