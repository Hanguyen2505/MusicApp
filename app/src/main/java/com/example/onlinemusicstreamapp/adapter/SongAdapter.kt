package com.example.onlinemusicstreamapp.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.exoplayer.MusicService
import com.example.onlinemusicstreamapp.databinding.CardviewSongBinding
import com.example.onlinemusicstreamapp.ui.activity.PlayerActivity

class SongAdapter (
    private var song: List<Song>
): RecyclerView.Adapter<SongAdapter.MyViewHolder>() {

    class MyViewHolder(
        private val binding: CardviewSongBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindData(song: Song) {
            binding.songTitle.text = song.title
            binding.desc.text = song.artist.toString()
            Glide.with(binding.songImage).load(song.imageUrl).into(binding.songImage)
            Log.d("soadapter", "${song.title}")

            binding.root.setOnClickListener {
                Log.d("binding", "$song")
                MusicService.startPlaying(binding.root.context, song).apply {

                }
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
        return song.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(song[position])

    }

    fun updateData(newSong: List<Song>) {
        song = newSong
        notifyDataSetChanged()
    }

}