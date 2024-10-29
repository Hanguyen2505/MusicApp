package com.example.onlinemusicstreamapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.databinding.CardviewSongBinding
import com.example.onlinemusicstreamapp.ui.activity.PlayerActivity

class SongAdapter : RecyclerView.Adapter<SongAdapter.MyViewHolder>() {

    var songs = emptyList<Song>()

    private var onItemClickListener: ((Song) -> Unit)? = null

    fun setOnItemClickListener(listener: (Song) -> Unit) {
        onItemClickListener = listener
    }

    class MyViewHolder(
        private val binding: CardviewSongBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindData(song: Song) {
            binding.songTitle.text = song.title
            binding.desc.text = song.artist.toString()
            Glide.with(binding.songImage).load(song.imageUrl).into(binding.songImage)
        }

        fun onClickMediaItem() {
            binding.root.setOnClickListener {
                it.context.startActivity(Intent(it.context, PlayerActivity::class.java))

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardviewSongBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentSong = songs[position]
        holder.bindData(currentSong)
        holder.onClickMediaItem()

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { click ->
                click(currentSong)
            }

        }

    }

    fun updateData(newSong: List<Song>) {
        songs = newSong
        notifyDataSetChanged()
    }

}