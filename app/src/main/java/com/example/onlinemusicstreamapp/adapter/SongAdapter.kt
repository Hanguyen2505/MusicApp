package com.example.onlinemusicstreamapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.database.data.entities.Song
import com.example.onlinemusicstreamapp.databinding.CardviewSongBinding

class SongAdapter(
    private val isBottomSheetDialog: Boolean
) : RecyclerView.Adapter<SongAdapter.MyViewHolder>() {

    private lateinit var songs: List<Song>

    //*Item Click Listener
    private var onItemClickListener: ((Song) -> Unit)? = null
    fun setOnItemClickListener(listener: (Song) -> Unit) {
        onItemClickListener = listener
    }
    //*More Button Click Listener
    private var onMoreButtonClickListener: ((Song) -> Unit)? = null
    fun setOnMoreButtonClickListener(listener: (Song) -> Unit) {
        onMoreButtonClickListener = listener
    }

    class MyViewHolder(
        private val binding: CardviewSongBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindData(song: Song) {
            binding.songTitle.text = song.title
            binding.desc.text = song.artist.joinToString(", ")
            Glide.with(binding.songImage).load(song.imageUrl).into(binding.songImage)
        }

        fun modifyIfIsBottomSheetDialog() {
            binding.moreBtn.setImageResource(R.drawable.ic_add)
        }

        fun moreBtn(onMoreButtonClick: () -> Unit) {
            binding.moreBtn.setOnClickListener {
                onMoreButtonClick()
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

        if (isBottomSheetDialog) {
            holder.modifyIfIsBottomSheetDialog()
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { click ->
                click(currentSong)
            }
        }

        holder.moreBtn {
            onMoreButtonClickListener?.invoke(currentSong)
        }
    }

    fun updateData(newSong: List<Song>) {
        songs = newSong
        notifyDataSetChanged()
    }

}