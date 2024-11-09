package com.example.onlinemusicstreamapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlinemusicstreamapp.database.data.entities.Artist
import com.example.onlinemusicstreamapp.databinding.CardviewArtistBinding


class ArtistAdapter: RecyclerView.Adapter<ArtistAdapter.MyViewHolder>() {

    var artist = emptyList<Artist>()

    private var onItemClickListener: ((Artist) -> Unit)? = null

    fun setOnItemClickListener(listener: (Artist) -> Unit) {
        onItemClickListener = listener
    }

    class MyViewHolder(
        private val binding: CardviewArtistBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindData(artist: Artist) {
            binding.artistName.text = artist.name
            Glide.with(binding.prfImage).load(artist.imageUrl).into(binding.prfImage)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardviewArtistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return artist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = artist[position]

        holder.bindData(currentItem)

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { click ->
                click(currentItem)
            }
        }
    }

    fun updateData(newArtist: List<Artist>) {
        artist = newArtist
        notifyDataSetChanged()
    }

}