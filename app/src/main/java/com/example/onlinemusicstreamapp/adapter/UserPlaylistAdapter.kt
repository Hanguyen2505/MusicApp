package com.example.onlinemusicstreamapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlinemusicstreamapp.database.data.entities.Playlist
import com.example.onlinemusicstreamapp.database.data.entities.UserPlaylist
import com.example.onlinemusicstreamapp.databinding.CardviewUserPlaylistBinding
import com.example.onlinemusicstreamapp.ui.fragments.library.LibraryFragmentDirections

class UserPlaylistAdapter: RecyclerView.Adapter<UserPlaylistAdapter.MyViewHolder>() {

    var playlist = emptyList<UserPlaylist>()

    private var onItemClickListener: ((UserPlaylist) -> Unit)? = null

    fun setOnItemClickListener(listener: (UserPlaylist) -> Unit) {
        onItemClickListener = listener
    }

    class MyViewHolder(
        private val binding: CardviewUserPlaylistBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindData(userPlaylist: UserPlaylist) {
            binding.playlistTitle.text = userPlaylist.title
            Glide.with(binding.coverImg).load(userPlaylist.coverUrl).into(binding.coverImg)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardviewUserPlaylistBinding.inflate(
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